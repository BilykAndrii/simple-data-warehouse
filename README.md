# simple-data-warehouse
Simple REST API application that exposes data - extracted from a csv file

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

GET "/api/info" returns info about application  

## File uploading  
CSV file could be uploaded as multipart-file by POST to "/upload" and persisted in embedded DB  
Mandatory param "dateFormat" (it needs for parsing dates correctly)      
Optional parameter "header" ("true" if file contain header)  
File is validated against "text/csv" Content Type  

It's possible to upload file/files many times  
DB has Primary Key contains from [Datasource, Campaign, Date], so data could be overwritten  

## Queries  
Example of queries:  
GET /api/metrics/query?metrics=impressions&groupedBy=date    
GET /api/metrics/query?metrics=ctr&groupedBy=dataSource,campaign    
GET /api/metrics/query?metrics=clicks&groupedBy=dataSource&dataSource=Twitter Ads&dateFrom=11/17/19&dateTo=11/19/19  

Also there is one endpoint to get all records (not filtered and not aggregated)  
GET /api/metrics/all  

### Parameters
"metrics" param could have one of {clicks, impressions, ctr} values  
'ctr' means "Click-Through Rate" = Clicks / Impressions * 100%  

"groupedBy" param could have one of {dataSource, campaign, date} values  

filters:  
"dataSource" param could be equal some value  
"campaign" param could be equal some value  
"dateFrom" and "dateTo" params are for period filtering, default Date format is "MM/dd/yy",   
but it possible to set it with "dateFormat" param, f.i. "dateTo=17-11-2019&dateFormat=dd-MM-yyyy"  

All unknown parameters will be ignored  

Queries are generic, but all dimensions and metrics are predefine in code,   
along with one calculated metric (click-throgh-rate).   
Introducing of another calculated metric will be quite easy,   
just need to implement formula and bind it with parameter name     


