import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Router} from '@angular/router';
import {NzTableQueryParams} from 'ng-zorro-antd/table';
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

  societyName: string = '';
  fromDate: string = '';
  toDate: string = '';

  tableData: FinanceTableRecord[] = [];
  // TODO add api to get total number of finance record within date range
  total: number = 0;
  pageSize: number = 10;
  pageIndex: number = 1;

  checked = false;
  loading = false;
  indeterminate = false;
  setOfCheckedId = new Set<string>();

  constructor(private router: Router, private apiService: ApiService) {
    this.financeTableRequestParam$ = new BehaviorSubject<FinanceTableRequestParam | null>(null);
  }

  ngOnInit(): void {
    // async monitoring parent when to submit form to check finance data
    this.financeTableRequestParam$.subscribe({
      next: (financeTableRequestParam: FinanceTableRequestParam | null) => {
        if (financeTableRequestParam !== null) {
          console.log(financeTableRequestParam);
          this.societyName = financeTableRequestParam.societyName;
          this.fromDate = financeTableRequestParam.fromDate;
          this.toDate = financeTableRequestParam.toDate;
          console.log(`fromDate: ${this.fromDate}, toDate: ${this.toDate}, societyName: ${this.societyName}`);
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
    console.log(params);
    const {pageSize, pageIndex, sort, filter} = params;
    const currentSort = sort.find(item => item.value !== null);
    const sortField = (currentSort && currentSort.key) || null;
    const sortOrder = (currentSort && currentSort.value) || null;
    console.log(sortField);
    console.log(sortOrder);
    console.log(pageSize);
    console.log(pageIndex);
    this.refreshCheckedStatus();
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
    console.log(requestData);
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
      this.apiService.getFinanceTableData(this.societyName, this.fromDate, this.toDate)
    ).subscribe(([totalNumber, tableData]) => {
      this.total = totalNumber.total;

      tableData.forEach((data: FinanceTableRecord) => (data.date = new Date(data.date).toDateString()));
      this.tableData = tableData;
    });
  }
}
