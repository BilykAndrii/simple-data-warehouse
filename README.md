# simple-data-warehouse
Simple REST API application that exposes data - extracted from a csv file.

API consumes these parameters:
- set of metrics (plus calculated ones) to be aggregated on
- an optional set of dimensions to be grouped by
- an optional set of dimension filters to be filtered on

Data looks like this:
- a time dimension (Date)
- regular dimensions (Campaign, Datasource)
- metrics (Clicks, Impressions)


# Host
Hosted on AWS   
http://simpledatawarehouse-env.eba-mfdpazxy.eu-central-1.elasticbeanstalk.com

# API 
described with Swagger UI  
http://simpledatawarehouse-env.eba-mfdpazxy.eu-central-1.elasticbeanstalk.com/api/swagger-ui/index.html

Examples of queries:

/api/metrics/query?metrics=impressions&groupedBy=date  
/api/metrics/query?metrics=ctr&groupedBy=dataSource,campaign  
/api/metrics/query?metrics=clicks&groupedBy=dataSource&dataSource=Twitter Ads&dateFrom=11/17/19&dateTo=11/19/19

