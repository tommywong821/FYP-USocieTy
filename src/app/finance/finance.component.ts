import {Component, ElementRef, HostListener, OnInit, Renderer2, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {BehaviorSubject, filter, zip} from 'rxjs';
import {ApiService} from '../services/api.service';
import {AuthService} from '../services/auth.service';
import {FinanceChartRecord} from './model/IFinanceChartRecord';
import {FinanceTableRequestParam} from './model/IFinanceTableParam';
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
  societyName: string = '';
  fromDate: string = '';
  toDate: string = '';
  financeTableRequestParam$: BehaviorSubject<FinanceTableRequestParam | null>;

  @ViewChild('flexContainer') containerRef!: ElementRef;
  ratio: number = 2.5;

  constructor(private authService: AuthService, private apiService: ApiService, private fb: FormBuilder) {
    this.currentDate = new Date();
    this.form = this.fb.group({
      societyName: ['', [Validators.required]],
      dateRange: [[], [Validators.required]],
    });
    this.financeTableRequestParam$ = new BehaviorSubject<FinanceTableRequestParam | null>(null);
  }

  ngOnInit(): void {
    // initial data picker value
    this.defaultDate = [this.getFirstDayOfYear(this.currentDate), this.getLastDayOfYear(this.currentDate)];

    this.authService.user$.pipe(filter(user => !!user)).subscribe(user => (this.enrolledSocieties = [...user!.roles]));
  }

  ngAfterViewInit(): void {
    this.checkFlexWrap();
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
      this.societyName = this.form.value.societyName;
      this.fromDate = this.form.value.dateRange[0].toLocaleDateString('en-US');
      this.toDate = this.form.value.dateRange[1].toLocaleDateString('en-US');
      console.log(`fromDate: ${this.fromDate}, toDate: ${this.toDate}, societyName: ${this.societyName}`);
      this.fetchFinanceRecord();
      this.financeTableRequestParam$.next({
        societyName: this.societyName,
        fromDate: this.fromDate,
        toDate: this.toDate,
      });
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

  fetchFinanceRecord() {
    zip(
      this.apiService.getFinancePieChartData(this.societyName, this.fromDate, this.toDate),
      this.apiService.getFinanceBarChartData(this.societyName, this.fromDate, this.toDate)
    ).subscribe(([pieChartData, barChartData]) => {
      this.pieChartData = pieChartData;
      this.barChartData = barChartData;
    });
  }

  @HostListener('window:resize')
  onWindowResize() {
    this.checkFlexWrap();
  }

  private checkFlexWrap() {
    const container = this.containerRef.nativeElement;

    // Get the actual width of the container
    const containerWidth = container.offsetWidth;

    // Get the total width of the flex items
    const itemsWidth = Array.from(container.children).reduce((totalWidth, item) => {
      //@ts-ignore
      return totalWidth + item.clientWidth;
    }, 0);

    // Compare the container width to the items width to detect wrapping
    if (containerWidth > 1536) {
      this.ratio = 2.5;
      //@ts-ignore
    } else if (itemsWidth > containerWidth) {
      console.log('Flex items wrapped!');
      this.ratio = 1;
      // Do something when wrapping occurs
    }
  }
}
