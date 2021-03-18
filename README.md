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


# Hosting
Hosted on AWS   
http://simpledatawarehouse-env.eba-mfdpazxy.eu-central-1.elasticbeanstalk.com

# API 
Swagger UI visualization 
http://simpledatawarehouse-env.eba-mfdpazxy.eu-central-1.elasticbeanstalk.com/api/swagger-ui/index.html

Examples of queries:

/api/metrics/query?metrics=impressions&groupedBy=date  
/api/metrics/query?metrics=ctr&groupedBy=dataSource,campaign  
/api/metrics/query?metrics=clicks&groupedBy=dataSource&dataSource=Twitter Ads&dateFrom=11/17/19&dateTo=11/19/19

"metrics" param could have one of {clicks, impressions, ctr} values
'ctr' means "Click-Through Rate" = Clicks / Impressions * 100%;

"groupedBy" param could have one of {dataSource, campaign, date} values

filters:
"dataSource" param could be equal some value
"campaign" param could be equal some value
"dateFrom" and "dateTo" params are for period filtering, default Date format is "MM/dd/yy", 
but it possible to set it with "dateFormat" param, f.i. "dateTo=17-11-2019&dateFormat=dd-MM-yyyy"

Queries are generic, but all dimensions and metrics are predefine in code, along with one calculated metric (click-throgh-rate). 
Introducing of another calculated metric will be quite easy - just need to implement formula and bind it with parameter name. 


