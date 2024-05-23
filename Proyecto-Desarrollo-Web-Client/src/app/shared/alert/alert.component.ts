import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-alert',
  templateUrl: './alert.component.html',
  styleUrl: './alert.component.css'
})
export class AlertComponent implements OnInit {

  message: string;

  constructor(public dialogRef: MatDialogRef<AlertComponent>, @Inject(MAT_DIALOG_DATA) public data: any) {
    this.message = data.message;
  }
  ngOnInit(): void {
    console.log(this.message);
  }


  closeDialog() {
    this.dialogRef.close();
  }
}
