import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {filter, zip} from 'rxjs';
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

  constructor(private authService: AuthService, private apiService: ApiService, private fb: FormBuilder) {
    this.currentDate = new Date();
    this.form = this.fb.group({
      societyName: ['', [Validators.required]],
      dateRange: [[], [Validators.required]],
    });
  }

  ngOnInit(): void {
    // initial data picker value
    this.defaultDate = [this.getFirstDayOfYear(this.currentDate), this.getLastDayOfYear(this.currentDate)];

    this.authService.user$
      .pipe(filter(user => !!user))
      .subscribe(user => (this.enrolledSocieties = [...user!.enrolledSocieties]));
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
      let societyName: string = this.form.value.societyName;
      let fromDate: string = this.form.value.dateRange[0].toLocaleDateString();
      let toDate: string = this.form.value.dateRange[1].toLocaleDateString();
      console.log(`fromDate: ${fromDate}, toDate: ${toDate}, societyName: ${societyName}`);
      this.fetchFinanceRecord(societyName, fromDate, toDate);
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

  fetchFinanceRecord(societyName: string, fromDate: string, toDate: string) {
    zip(
      this.apiService.getFinanceTableData(societyName, fromDate, toDate),
      this.apiService.getFinancePieChartData(societyName, fromDate, toDate),
      this.apiService.getFinanceBarChartData(societyName, fromDate, toDate)
    ).subscribe(([tableData, pieChartData, barChartData]) => {
      this.tableData = tableData;
      this.pieChartData = pieChartData;
      this.barChartData = barChartData;
    });
  }
}
