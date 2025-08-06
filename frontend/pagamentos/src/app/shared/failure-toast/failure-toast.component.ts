import { Component, Inject, OnInit } from '@angular/core';
import { MAT_SNACK_BAR_DATA, MatSnackBarRef } from '@angular/material/snack-bar';

@Component({
  selector: 'app-failure-toast',
  templateUrl: './failure-toast.component.html',
  styleUrls: ['./failure-toast.component.scss']
})
export class FailureToastComponent implements OnInit {

  constructor(
    public snackBarRef: MatSnackBarRef<FailureToastComponent>,
    @Inject(MAT_SNACK_BAR_DATA) public data: any
  ) { }

  ngOnInit(): void {
  }

}
