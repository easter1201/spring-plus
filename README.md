# SPRING PLUS

## Lv.12 AWS 활용 첨부내용  

### Lv.12-1 EC2  
#### EC2 Instance  
![ec2Instance.png](ec2Instance.png)  
#### EC2 Inbound  
![ec2SecurityInbound.png](ec2SecurityInbound.png)
#### HealthCheckApi 실행 결과 
![healthcheckapi.png](healthcheckapi.png)
##### Web 실행  
![postman.png](postman.png)
##### Postman 실행  

### Lv.12-2 RDS  
#### RDS Setting
![rdsDB.png](rdsDB.png)    
#### RDS Connect  
![rds 연동.png](rds%20%EC%97%B0%EB%8F%99.png)  

### Lv.12-3 S3
#### Bucket Setting  
![s3bucket.png](s3bucket.png)  
#### Bucket Connect 결과  
![bucket.png](bucket.png)

## Lv.13 대용량 데이터 처리 첨부내용
### 초기 조회 속도
![nonindexing.png](nonindexing.png)  

### 인덱싱 사용
![indexing.png](indexing.png)  

### 특정 컬럼(id, email, nickname)만 검색  
![specificColumn.png](specificColumn.png)  

### 인덱싱 + 특정 컬럼만 검색
![indexingPlusSpecificColumn.png](indexingPlusSpecificColumn.png)
#### 결과
유의미하게 결과에 차이가 나는 것은 인덱싱 기능을 사용했을 때 이며,  
특정 컬럼만 검색의 경우 약 3~5ms 정도의 차이만 보였다.
