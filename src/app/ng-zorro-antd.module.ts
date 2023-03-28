import {NgModule} from '@angular/core';
import {NzButtonModule} from 'ng-zorro-antd/button';
import {NzDatePickerModule} from 'ng-zorro-antd/date-picker';
import {NzFormModule} from 'ng-zorro-antd/form';
import {NzIconModule} from 'ng-zorro-antd/icon';
import {NzInputModule} from 'ng-zorro-antd/input';
import {NzMessageModule} from 'ng-zorro-antd/message';
import {NzSelectModule} from 'ng-zorro-antd/select';
import {NzTableModule} from 'ng-zorro-antd/table';
import {NzPaginationModule} from 'ng-zorro-antd/pagination';
import {NzUploadModule} from 'ng-zorro-antd/upload';
import {NzModalModule} from 'ng-zorro-antd/modal';
import {NzDescriptionsModule} from 'ng-zorro-antd/descriptions';
import {NzBadgeModule} from 'ng-zorro-antd/badge';
import {NzDividerModule} from 'ng-zorro-antd/divider';

@NgModule({
  imports: [
    NzIconModule,
    NzDatePickerModule,
    NzInputModule,
    NzButtonModule,
    NzFormModule,
    NzUploadModule,
    NzMessageModule,
    NzSelectModule,
    NzTableModule,
    NzPaginationModule,
    NzModalModule,
    NzDescriptionsModule,
    NzBadgeModule,
    NzDividerModule,
  ],
  exports: [
    NzIconModule,
    NzDatePickerModule,
    NzInputModule,
    NzButtonModule,
    NzFormModule,
    NzUploadModule,
    NzMessageModule,
    NzSelectModule,
    NzTableModule,
    NzPaginationModule,
    NzModalModule,
    NzDescriptionsModule,
    NzBadgeModule,
    NzDividerModule,
  ],
})
export class NgZorroAntdModule {}
