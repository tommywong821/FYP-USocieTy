import {Component, ElementRef, EventEmitter, OnInit, ViewContainerRef} from '@angular/core';

@Component({
  selector: 'app-finance-pie-chart',
  templateUrl: './finance-pie-chart.component.html',
  styleUrls: ['./finance-pie-chart.component.scss'],
})
export class FinancePieChartComponent implements OnInit {
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
  constructor(public elementRef: ElementRef) {}

  ngOnInit(): void {}

  onSelect(data: EventEmitter<any>): void {
    console.log('Item clicked', JSON.parse(JSON.stringify(data)));
  }

  onActivate(data: EventEmitter<any>): void {
    console.log('Activate', JSON.parse(JSON.stringify(data)));
  }

  onDeactivate(data: EventEmitter<any>): void {
    console.log('Deactivate', JSON.parse(JSON.stringify(data)));
  }

  valueFormatting(value: number) {
    return `$${value}`;
  }
}
