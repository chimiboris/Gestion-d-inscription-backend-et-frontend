import { Component, OnInit } from '@angular/core';
import { AdminStatistiqueService } from './admin-statistique.service';
import { CommonModule } from '@angular/common';
import {Color, NgxChartsModule} from '@swimlane/ngx-charts';
import { ScaleType } from '@swimlane/ngx-charts';



@Component({
  selector: 'jhi-admin-dashboard',
  standalone: true,
  templateUrl: './admin-dashboard.component.html',
  imports: [
    CommonModule,
    NgxChartsModule,
  ],
})


export class AdminDashboardComponent implements OnInit {

  stats: Record<string, number> = {};
  chartData: { name: string; value: number }[] = [];

  colorScheme: Color = {
    name: 'myCustomScheme',
    selectable: true,
    group: ScaleType.Ordinal,
    domain: ['#5AA454', '#A10A28', '#C7B42C', '#AAAAAA', '#003f5c', '#7a5195']
  };


  constructor(private adminstatistiqueService: AdminStatistiqueService) {}

  ngOnInit(): void {
    this.adminstatistiqueService.getStatistiques().subscribe(data => {
      this.stats = data;

      this.chartData = Object.entries(data).map(([key, value]) => ({
        name: key,
        value: value,
      }));
    });
  }

}

