import {Component, EventEmitter, Input, OnInit} from '@angular/core';
import {FinanceChartRecord} from '../IFinanceChartRecord';

@Component({
  selector: 'app-finance-bar-chart',
  templateUrl: './finance-bar-chart.component.html',
  styleUrls: ['./finance-bar-chart.component.scss'],
})
export class FinanceBarChartComponent implements OnInit {
  @Input() barChartData: FinanceChartRecord[] = [];

  view: [number, number];

  constructor() {
    this.view = [innerWidth / 2.5, 400];
  }

  ngOnInit(): void {}

  onSelect(data: EventEmitter<any>): void {
    console.log('Item clicked', JSON.parse(JSON.stringify(data)));
  }

  // view is the variable used to change the chart size (Ex: view = [width, height])

  onResize(event: any) {
    this.view = [event.target.innerWidth / 2.5, 400];
  }
}
