import {Component, EventEmitter, OnInit} from '@angular/core';

@Component({
  selector: 'app-finance-bar-chart',
  templateUrl: './finance-bar-chart.component.html',
  styleUrls: ['./finance-bar-chart.component.scss'],
})
export class FinanceBarChartComponent implements OnInit {
  barChartData = [
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

  constructor() {}

  ngOnInit(): void {}

  onSelect(data: EventEmitter<any>): void {
    console.log('Item clicked', JSON.parse(JSON.stringify(data)));
  }
}
