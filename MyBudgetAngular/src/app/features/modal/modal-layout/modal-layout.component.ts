import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-modal-layout',
  standalone: true,
  imports: [],
  templateUrl: './modal-layout.component.html',
  styleUrl: './modal-layout.component.css'
})
export class ModalLayoutComponent {

  constructor(
    public dialogRef: MatDialogRef<ModalLayoutComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {}

  onClose(): void {
    this.dialogRef.close();
  }
  
}
