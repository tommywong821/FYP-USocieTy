import {Component, EventEmitter, Input, OnInit} from '@angular/core';
import {FinanceChartRecord} from '../IFinanceChartRecord';

@Component({
  selector: 'app-finance-bar-chart',
  templateUrl: './finance-bar-chart.component.html',
  styleUrls: ['./finance-bar-chart.component.scss'],
})
export class FinanceBarChartComponent implements OnInit {
  @Input() barChartData: FinanceChartRecord[] = [];

  constructor() {}

  ngOnInit(): void {}

  onSelect(data: EventEmitter<any>): void {
    console.log('Item clicked', JSON.parse(JSON.stringify(data)));
  }
}
