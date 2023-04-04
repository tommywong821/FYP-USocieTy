import {Component, ElementRef, EventEmitter, Input, OnInit} from '@angular/core';
import {FinanceChartRecord} from '../model/IFinanceChartRecord';

@Component({
  selector: 'app-finance-pie-chart',
  templateUrl: './finance-pie-chart.component.html',
  styleUrls: ['./finance-pie-chart.component.scss'],
})
export class FinancePieChartComponent implements OnInit {
  @Input() pieChartData: FinanceChartRecord[] = [];
  @Input() ratio!: number;
  view: [number, number];

  constructor() {
    this.view = [innerWidth / this.ratio, 400];
  }

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
    return `$${value.toLocaleString('en-US')}`;
  }

  onResize(event: any) {
    this.view = [event.target.innerWidth / this.ratio, 400];
  }
}
