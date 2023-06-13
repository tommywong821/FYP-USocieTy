export interface FinanceChartRecord {
  // bar chart: name == group by month
  // pie chart: name == group by category
  name: string;
  value: number;
}
