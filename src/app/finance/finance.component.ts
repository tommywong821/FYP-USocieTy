import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {filter} from 'rxjs';
import {ApiService} from '../services/api.service';
import {AuthService} from '../services/auth.service';
import {FinanceChartRecord} from './model/IFinanceChartRecord';
import {FinanceTableRecord} from './model/IFinanceTableRecord';

@Component({
  selector: 'app-finance',
  templateUrl: './finance.component.html',
  styleUrls: ['./finance.component.scss'],
})
export class FinanceComponent implements OnInit {
  currentDate: Date;
  // defaultDate[0]: fromDate, defaultDate[1]: toDate
  defaultDate: Date[] = [];
  barChartData: FinanceChartRecord[] = [];
  pieChartData: FinanceChartRecord[] = [];
  tableData: FinanceTableRecord[] = [];

  enrolledSocieties: string[] = [];

  form!: FormGroup;
  fromDate: String = '';
  toDate: String = '';
  societyName: String = '';

  constructor(private authService: AuthService, private apiService: ApiService, private fb: FormBuilder) {
    this.currentDate = new Date();
    this.form = this.fb.group({
      societyName: ['', [Validators.required]],
      dateRange: [[], [Validators.required]],
    });
  }

  ngOnInit(): void {
    // dummy
    this.defaultDate = [this.getFirstDayOfYear(this.currentDate), this.getLastDayOfYear(this.currentDate)];

    this.authService.user$
      .pipe(filter(user => !!user))
      .subscribe(user => (this.enrolledSocieties = [...user!.enrolledSocieties]));
  }

  onDateChange(result: Date[]): void {
    console.log(result[0].toLocaleDateString());
    //TODO change to call api with change date
    this.barChartData = [
      {
        name: 'January-2023',
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

  submitForm(): void {
    if (this.form.valid) {
      //change date format to mm/dd/2023
      this.fromDate = this.form.value.dateRange[0].toLocaleDateString();
      this.toDate = this.form.value.dateRange[1].toLocaleDateString();
      this.societyName = this.form.value.societyName;
      console.log(`fromDate: ${this.fromDate}, toDate: ${this.toDate}, societyName: ${this.societyName}`);
    } else {
      alert('You must fill all the fields');
      Object.values(this.form.controls).forEach(control => {
        if (control.invalid) {
          control.markAsDirty();
          control.updateValueAndValidity({onlySelf: true});
        }
      });
    }
  }

  fetchFinanceRecord() {}
}
