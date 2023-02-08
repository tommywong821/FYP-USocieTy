import {Component, Input, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {Path} from 'src/app/app-routing.module';
import {ApiService} from 'src/app/services/api.service';
import {FinanceTableRecord} from '../model/IFinanceTableRecord';

@Component({
  selector: 'app-finance-table',
  templateUrl: './finance-table.component.html',
  styleUrls: ['./finance-table.component.scss'],
})
export class FinanceTableComponent implements OnInit {
  @Input() tableData: FinanceTableRecord[] = [];
  @Input() societyName: string = '';

  checked = false;
  loading = false;
  indeterminate = false;
  listOfCurrentPageData: readonly FinanceTableRecord[] = [];
  setOfCheckedId = new Set<string>();

  constructor(private router: Router, private apiService: ApiService) {}

  ngOnInit(): void {}

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
}
