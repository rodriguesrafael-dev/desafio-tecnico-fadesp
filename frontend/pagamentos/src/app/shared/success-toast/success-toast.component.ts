import { Component, Inject, OnInit } from '@angular/core';
import { MAT_SNACK_BAR_DATA, MatSnackBarRef } from '@angular/material/snack-bar';

@Component({
  selector: 'app-success-toast',
  templateUrl: './success-toast.component.html',
  styleUrls: ['./success-toast.component.scss']
})
export class SuccessToastComponent implements OnInit {

  constructor(
    public snackBarRef: MatSnackBarRef<SuccessToastComponent>,
    @Inject(MAT_SNACK_BAR_DATA) public data: any
  ) { }

  ngOnInit(): void {
  }

}
