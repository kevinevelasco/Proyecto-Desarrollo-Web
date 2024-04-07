import { Compiler, Component, OnInit } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';
import { Pipe, PipeTransform } from '@angular/core'; 
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrl: './admin.component.css',
})
export class AdminComponent implements OnInit {
  //TODO realizar la implementación para mostrar todo lo que se realizó para el admin
  ngOnInit(): void {
  }
}
