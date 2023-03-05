import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Router} from '@angular/router';
import {NzTableFilterList, NzTableQueryParams} from 'ng-zorro-antd/table';
import {BehaviorSubject, zip} from 'rxjs';
import {Path} from 'src/app/app-routing.module';
import {ApiService} from 'src/app/services/api.service';
import {FinanceTableRequestParam} from '../model/IFinanceTableParam';
import {FinanceTableRecord} from '../model/IFinanceTableRecord';

@Component({
  selector: 'app-finance-table',
  templateUrl: './finance-table.component.html',
  styleUrls: ['./finance-table.component.scss'],
})
export class FinanceTableComponent implements OnInit {
  @Input() financeTableRequestParam$: BehaviorSubject<FinanceTableRequestParam | null>;

  @Output() updateFinanceDataEvent = new EventEmitter<void>();

  societyName = '';
  fromDate = '';
  toDate = '';

  tableData: FinanceTableRecord[] = [];
  total = 0;
  pageSize = 10;
  pageIndex = 1;
  filterCategory: NzTableFilterList = [];

  checked = false;
  loading = false;
  indeterminate = false;
  setOfCheckedId = new Set<string>();
  // block api call when component init
  queryParamsChangeEventCnt = 0;

  constructor(private router: Router, private apiService: ApiService) {
    this.financeTableRequestParam$ = new BehaviorSubject<FinanceTableRequestParam | null>(null);
  }

  ngOnInit(): void {
    // async monitoring parent when to submit form to check finance data
    this.financeTableRequestParam$.subscribe({
      next: (financeTableRequestParam: FinanceTableRequestParam | null) => {
        if (financeTableRequestParam !== null) {
          this.societyName = financeTableRequestParam.societyName;
          this.fromDate = financeTableRequestParam.fromDate;
          this.toDate = financeTableRequestParam.toDate;
          this.fetchTableData();
        }
      },
    });
  }

  updateCheckedSet(id: string, checked: boolean): void {
    if (checked) {
      this.setOfCheckedId.add(id);
    } else {
      this.setOfCheckedId.delete(id);
    }
  }

  onQueryParamsChange(params: NzTableQueryParams): void {
    // block api call when component init
    if (++this.queryParamsChangeEventCnt == 1) {
      return;
    }

    const {pageSize, pageIndex, sort, filter} = params;
    const currentSort = sort.find(item => item.value !== null);
    const sortField = (currentSort && currentSort.key) || undefined;
    const sortOrder = (currentSort && currentSort.value) || undefined;
    const currentFilter = filter.find(item => item.value !== null);
    const filterKey = (currentFilter && currentFilter.key) || undefined;
    const filterValue = (currentFilter && currentFilter.value) || undefined;

    if (filterValue.length) {
      this.apiService
        .getTotalNumberOfFinanceTableData(this.societyName, this.fromDate, this.toDate, filterKey, filterValue)
        .subscribe({
          next: totalNumber => {
            this.total = totalNumber.total;
          },
        });
    }

    this.apiService
      .getFinanceTableData(
        this.societyName,
        this.fromDate,
        this.toDate,
        pageIndex,
        pageSize,
        sortField,
        sortOrder,
        filterKey,
        filterValue
      )
      .subscribe({
        next: tableData => {
          tableData.forEach((data: FinanceTableRecord) => (data.date = new Date(data.date).toDateString()));
          this.tableData = tableData;
          this.refreshCheckedStatus();
        },
      });
  }

  refreshCheckedStatus(): void {
    const listOfEnabledData = this.tableData.filter(({disabled}) => !disabled);
    this.checked = listOfEnabledData.every(({id}) => this.setOfCheckedId.has(id));
    this.indeterminate = listOfEnabledData.some(({id}) => this.setOfCheckedId.has(id)) && !this.checked;
  }

  onAllChecked(checked: boolean): void {
    this.tableData.filter(({disabled}) => !disabled).forEach(({id}) => this.updateCheckedSet(id, checked));
    this.refreshCheckedStatus();
  }

  sendRequest(): void {
    this.loading = true;
    const requestData = this.tableData.filter(data => this.setOfCheckedId.has(data.id));
    this.apiService
      .deleteFinanceData(
        this.societyName,
        requestData.map(data => data.id)
      )
      .subscribe({
        next: () => {
          this.setOfCheckedId.clear();
          this.refreshCheckedStatus();
          this.loading = false;
          // update parent data
          this.updateFinanceDataEvent.emit();
          this.fetchTableData();
        },
      });
  }

  onItemChecked(id: string, checked: boolean): void {
    this.updateCheckedSet(id, checked);
    this.refreshCheckedStatus();
  }

  routeToCreateRecordPage() {
    this.router.navigate([Path.Main, Path.Finance, Path.CreateFinance]);
  }

  fetchTableData() {
    zip(
      this.apiService.getTotalNumberOfFinanceTableData(this.societyName, this.fromDate, this.toDate),
      this.apiService.getFinanceTableData(this.societyName, this.fromDate, this.toDate),
      this.apiService.getAllCategoryOfFinanceRecord(this.societyName, this.fromDate, this.toDate)
    ).subscribe(([totalNumber, tableData, categoryList]) => {
      this.total = totalNumber.total;

      tableData.forEach((data: FinanceTableRecord) => (data.date = new Date(data.date).toDateString()));
      this.tableData = tableData;

      this.filterCategory = categoryList;
      console.log(this.filterCategory);
    });
  }
}
