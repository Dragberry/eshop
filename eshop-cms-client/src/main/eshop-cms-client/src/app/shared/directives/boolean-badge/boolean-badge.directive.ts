import { Directive, Input, Renderer, ElementRef, OnChanges, SimpleChange, SimpleChanges } from '@angular/core';

@Directive({
  selector: '[appBooleanBadge]'
})
export class BooleanBadgeDirective implements OnChanges {

  @Input()
  badgeValue: boolean;

  @Input()
  trueClass = 'badge-success';

  @Input()
  falseClass = 'badge-danger';

  constructor(private renderer: Renderer, private el: ElementRef) {
    renderer.setElementClass(el.nativeElement, 'w-100', true);
    renderer.setElementClass(el.nativeElement, 'badge', true);
  }

  ngOnChanges(changes: SimpleChanges) {
    const change: SimpleChange = changes['badgeValue'];
    if (change) {
      if (change.currentValue) {
        this.renderer.setElementClass(this.el.nativeElement, this.trueClass, true);
      } else {
        this.renderer.setElementClass(this.el.nativeElement, this.falseClass, true);
      }
    }
  }
}
