import {Component, EventEmitter, OnInit} from '@angular/core';

@Component({
  selector: 'app-finance',
  templateUrl: './finance.component.html',
  styleUrls: ['./finance.component.scss'],
})
export class FinanceComponent implements OnInit {
  view: [number, number] = [700, 400];
  pieChartData = [
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
  gradient: boolean = true;

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

  ngOnInit(): void {
    // dummy
  }

  valueFormatting(value: number) {
    return `$${value}`;
  }

  onSelect(data: EventEmitter<any>): void {
    console.log('Item clicked', JSON.parse(JSON.stringify(data)));
  }

  onActivate(data: EventEmitter<any>): void {
    console.log('Activate', JSON.parse(JSON.stringify(data)));
  }

  onDeactivate(data: EventEmitter<any>): void {
    console.log('Deactivate', JSON.parse(JSON.stringify(data)));
  }
}
