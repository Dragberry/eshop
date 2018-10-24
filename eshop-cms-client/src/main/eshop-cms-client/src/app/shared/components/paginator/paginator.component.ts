import { Component, OnInit, Input } from '@angular/core';
import { Page } from '../../model/page';

@Component({
  selector: 'app-paginator',
  templateUrl: './paginator.component.html',
  styleUrls: ['./paginator.component.css']
})
export class PaginatorComponent implements OnInit {

  @Input() page: Page<any>;

  constructor() { }

  ngOnInit() {
  }

}
