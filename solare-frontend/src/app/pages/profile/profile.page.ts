import { DatePipe } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CopCurrencyPipe } from '../../pipes/cop-currency.pipe';
import { AuthService } from '../../services/auth.service';
import { OrderService } from '../../services/order.service';
import { UserProfile } from '../../models/auth.model';
import { Order } from '../../models/order.model';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [DatePipe, CopCurrencyPipe],
  templateUrl: './profile.page.html',
})
export class ProfilePage implements OnInit {
  private readonly auth = inject(AuthService);
  private readonly orders = inject(OrderService);
  private readonly route = inject(ActivatedRoute);

  protected readonly profile = signal<UserProfile | null>(null);
  protected readonly orderList = signal<Order[]>([]);
  protected readonly ok = signal(false);

  ngOnInit(): void {
    if (this.route.snapshot.queryParamMap.get('ok')) {
      this.ok.set(true);
    }
    this.auth.me().subscribe((p) => this.profile.set(p));
    this.orders.mine().subscribe((o) => this.orderList.set(o));
  }
}
