import {Component, EventEmitter, OnInit} from '@angular/core';

@Component({
  selector: 'app-finance',
  templateUrl: './finance.component.html',
  styleUrls: ['./finance.component.scss'],
})
export class FinanceComponent implements OnInit {
  view: [number, number] = [700, 400];
  data = [
    {
      name: 'Germany',
      value: 8940000,
    },
    {
      name: 'USA',
      value: 5000000,
    },
    {
      name: 'France',
      value: 7200000,
    },
    {
      name: 'UK',
      value: 6200000,
    },
  ];
  gradient: boolean = true;

  constructor() {}

  ngOnInit(): void {
    // dummy
  }

  valueFormatting(value: number) {
    return `HKD: ${value}`;
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
