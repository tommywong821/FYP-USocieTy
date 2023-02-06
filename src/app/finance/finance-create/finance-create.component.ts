import {Component, OnInit} from '@angular/core';
import {Form, FormArray, FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {IconProp} from '@fortawesome/fontawesome-svg-core';
import {faMinus} from '@fortawesome/free-solid-svg-icons';
import {filter} from 'rxjs';
import {User} from 'src/app/model/user';
import {AuthService} from 'src/app/services/auth.service';

@Component({
  selector: 'app-finance-create',
  templateUrl: './finance-create.component.html',
  styleUrls: ['./finance-create.component.scss'],
})
export class FinanceCreateComponent implements OnInit {
  validateForm!: FormGroup;
  faMinus: IconProp = faMinus;

  enrolledSocieties: string[] = [];

  constructor(private fb: FormBuilder, private authService: AuthService) {
    this.validateForm = this.fb.group({
      societyName: '',
      financeRecords: this.fb.array([]),
    });
  }

  get financeRecords(): FormArray {
    return this.validateForm.get('financeRecords') as FormArray;
  }

  ngOnInit(): void {
    this.authService.user$
      .pipe(filter(user => !!user))
      .subscribe(user => (this.enrolledSocieties = [...user!.enrolledSocieties]));

    this.addField();
  }

  addField(e?: MouseEvent): void {
    if (e) {
      e.preventDefault();
    }
    this.financeRecords.push(
      // //Validate user input
      this.fb.group({
        amount: new FormControl(null, [Validators.required, Validators.pattern('^[0-9]*$')]),
        category: new FormControl(null, Validators.required),
        description: new FormControl(null, Validators.required),
        date: new FormControl(null, Validators.required),
      })
    );
  }

  removeFinanceRecrod(index: number): void {
    if (this.financeRecords.value.length > 1) {
      (<FormArray>this.validateForm.get('financeRecords')).removeAt(index);
    }
  }

  submitForm(): void {
    if (this.validateForm.valid) {
      console.log('submit', this.validateForm.value);
      //change date format to mm/dd/2023
      this.validateForm.value.financeRecords.forEach((financeRecord: any) => {
        financeRecord.date = financeRecord.date.toLocaleDateString();
      });
      console.log('modify', this.validateForm.value);
    } else {
      alert('You must fill all the fields');
      Object.values(this.financeRecords.controls).forEach(control => {
        if (control.invalid) {
          control.markAsDirty();
          control.updateValueAndValidity({onlySelf: true});
        }
      });
    }
  }
}
