import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {IconProp} from '@fortawesome/fontawesome-svg-core';
import {faMinus} from '@fortawesome/free-solid-svg-icons';

export interface IFinanceRecordForm {
  id: number;
  amount: string;
  description: string;
  date: string;
}
@Component({
  selector: 'app-finance-create',
  templateUrl: './finance-create.component.html',
  styleUrls: ['./finance-create.component.scss'],
})
export class FinanceCreateComponent implements OnInit {
  validateForm!: FormGroup;
  listOfFinanceRecord: Array<IFinanceRecordForm> = [];
  faMinus: IconProp = faMinus;

  constructor(private fb: FormBuilder) {}

  ngOnInit(): void {
    this.validateForm = this.fb.group({});
    this.addField();
  }

  addField(e?: MouseEvent): void {
    if (e) {
      e.preventDefault();
    }
    const id =
      this.listOfFinanceRecord.length > 0 ? this.listOfFinanceRecord[this.listOfFinanceRecord.length - 1].id + 1 : 0;

    const control = {
      id,
      date: `${id}date`,
      amount: `${id}amount`,
      description: `${id}description`,
    };
    const index = this.listOfFinanceRecord.push(control);
    console.log(this.listOfFinanceRecord[this.listOfFinanceRecord.length - 1]);
    //Validate user input
    this.validateForm.addControl(
      this.listOfFinanceRecord[index - 1].amount,
      new FormControl(null, [Validators.required, Validators.pattern('^[0-9]*$')])
    );
    this.validateForm.addControl(this.listOfFinanceRecord[index - 1].date, new FormControl(null, Validators.required));
    this.validateForm.addControl(
      this.listOfFinanceRecord[index - 1].description,
      new FormControl(null, Validators.required)
    );
  }

  removeField(item: IFinanceRecordForm, e: MouseEvent): void {
    e.preventDefault();
    if (this.listOfFinanceRecord.length > 1) {
      const index = this.listOfFinanceRecord.indexOf(item);
      this.listOfFinanceRecord.splice(index, 1);
      console.log(this.listOfFinanceRecord);
      this.validateForm.removeControl(item.amount);
      this.validateForm.removeControl(item.date);
      this.validateForm.removeControl(item.description);
    }
  }

  submitForm(): void {
    if (this.validateForm.valid) {
      console.log('submit', this.validateForm.value);
      //TODO group object: current onlt fields
    } else {
      Object.values(this.validateForm.controls).forEach(control => {
        if (control.invalid) {
          control.markAsDirty();
          control.updateValueAndValidity({onlySelf: true});
        }
      });
    }
  }
}
