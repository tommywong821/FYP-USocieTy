import {Component, OnInit} from '@angular/core';
import {FinanceChartRecord} from './IFinanceChartRecord';
import {FinanceTableRecord} from './IFinanceTableRecord';

@Component({
  selector: 'app-finance',
  templateUrl: './finance.component.html',
  styleUrls: ['./finance.component.scss'],
})
export class FinanceComponent implements OnInit {
  currentDate: Date;
  defaultDate: Date[] = [];
  barChartData: FinanceChartRecord[] = [];
  pieChartData: FinanceChartRecord[] = [];
  tableData: FinanceTableRecord[] = [];

  constructor() {
    this.currentDate = new Date();
  }

  ngOnInit(): void {
    // dummy
    this.defaultDate = [this.getFirstDayOfYear(this.currentDate), this.getLastDayOfYear(this.currentDate)];
    //TODO change to call api with default date
  }

  onDateChange(result: Date[]): void {
    //TODO change to call api with change date
    this.barChartData = [
      {
        name: 'January',
        value: 7187,
      },
      {
        name: 'February',
        value: 8738,
      },
      {
        name: 'March',
        value: 408,
      },
      {
        name: 'April',
        value: 5490,
      },
      {
        name: 'May',
        value: 9057,
      },
      {
        name: 'June',
        value: 4117,
      },
      {
        name: 'July',
        value: 7331,
      },
      {
        name: 'August',
        value: 8421,
      },
      {
        name: 'September',
        value: 4450,
      },
      {
        name: 'October',
        value: 1852,
      },
      {
        name: 'November',
        value: 6738,
      },
      {
        name: 'December',
        value: 2062,
      },
    ];

    this.pieChartData = [
      {
        name: 'Souvenir',
        value: 5740,
      },
      {
        name: 'Supplies',
        value: 4736,
      },
      {
        name: 'Daily Expenses',
        value: 5301,
      },
      {
        name: 'Maintenance ',
        value: 7913,
      },
    ];

    this.tableData = new Array(100).fill(0).map((_, index) => ({
      id: index,
      date: `Edward King ${index}`,
      amount: 32,
      description: `London, Park Lane no. ${index}`,
      editBy: `edit by ${index}`,
      disabled: index % 2 === 0,
    }));
  }

  getFirstDayOfYear(date: Date): Date {
    return new Date(date.getFullYear(), 0, 1);
  }

  getLastDayOfYear(date: Date): Date {
    return new Date(date.getFullYear(), 11, 31);
  }
}
