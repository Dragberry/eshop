import { Directive, Input, Renderer, ElementRef } from '@angular/core';

@Directive({
  selector: '[appBooleanBadge]'
})
export class BooleanBadgeDirective {

  @Input()
  trueClass = 'badge-success';

  @Input()
  falseClass = 'badge-danger';

  constructor(private renderer: Renderer, private el: ElementRef) {
    renderer.setElementClass(el.nativeElement, 'w-100', true);
    renderer.setElementClass(el.nativeElement, 'badge', true);
  }

  @Input()
  set badgeValue(badgeValue: boolean) {
    this.renderer.setElementClass(this.el.nativeElement, this.trueClass, badgeValue);
    this.renderer.setElementClass(this.el.nativeElement, this.falseClass, !badgeValue);
  }

}
