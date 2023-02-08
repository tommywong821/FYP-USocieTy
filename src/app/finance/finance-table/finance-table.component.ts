import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Router} from '@angular/router';
import {BehaviorSubject} from 'rxjs';
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

  checked = false;
  loading = false;
  indeterminate = false;
  listOfCurrentPageData: readonly FinanceTableRecord[] = [];
  setOfCheckedId = new Set<string>();

  constructor(private router: Router, private apiService: ApiService) {
    this.financeTableRequestParam$ = new BehaviorSubject<FinanceTableRequestParam | null>(null);
  }

  ngOnInit(): void {
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

  onCurrentPageDataChange(listOfCurrentPageData: readonly FinanceTableRecord[]): void {
    this.listOfCurrentPageData = listOfCurrentPageData;
    this.refreshCheckedStatus();
  }

  refreshCheckedStatus(): void {
    const listOfEnabledData = this.listOfCurrentPageData.filter(({disabled}) => !disabled);
    this.checked = listOfEnabledData.every(({id}) => this.setOfCheckedId.has(id));
    this.indeterminate = listOfEnabledData.some(({id}) => this.setOfCheckedId.has(id)) && !this.checked;
  }

  onAllChecked(checked: boolean): void {
    this.listOfCurrentPageData.filter(({disabled}) => !disabled).forEach(({id}) => this.updateCheckedSet(id, checked));
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
          // TODO update ui
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
    this.apiService.getFinanceTableData(this.societyName, this.fromDate, this.toDate).subscribe({
      next: (tableData: FinanceTableRecord[]) => {
        tableData.forEach((data: FinanceTableRecord) => (data.date = new Date(data.date).toDateString()));
        this.tableData = tableData;
      },
    });
  }
}
