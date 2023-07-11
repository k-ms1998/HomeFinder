# 🏠 HomeFinder

## 1. 개발 기간
- 2023년 4월 10일 ~ 진행중

</br>

## 2. 사용 기술
#### `Back-end`
  - Java 17
  - Spring Boot 3.0.1
  - Spring Data JPA
  - Spring Batch
  - Swagger
  - Webflux
  - Validation
  - Lombok
  - JAXB
  - Gradle
  - MySQL
#### `API`
  - TMap 대중교통 API
  - Kakao Developers 로컬 API
  - 공공데이터 공동주택 단지 목록 제공 API
  - 공공데이터 공동주택 기본 정보 제공 API

![image](https://github.com/k-ms1998/HomeFinder/assets/71029015/4bdbc386-43cf-490a-8588-f91f84a7bec9)


</br>

## 3. 프로젝트 개요 및 핵심 기능
#### `프로젝트 개요`
해당 프로젝트는 가족 구성원들의 각각 다른 근무지 때문에 거주지를 정하는데 어려움을 겪는 부분을 해소하기 위해 시작한 프로젝트입니다.
각 가족 구성원들의 근무지와 거주지로 부터 각 근무지까지의 희망하는 소요 시간을 입력하면, 조건을 만족시키는 아파트들의 목록을 반환해주는 기능을 합니다.

#### `핵심 기능`
  - 입력 받은 좌표로 부터 가장 가까운 지하철역 구하기
  - 시작 지하철역에서 사용자가 입력한 시간내에 도달 가능한 모든 지하쳘역들 찾기
  - 지하철역에서 특정 시간내에 도달 가능한 모든 아파트 찾기

</br>

## 4. ERD
 ![제목 없는 다이어그램 (3)](https://github.com/k-ms1998/HomeFinder/assets/71029015/132a221d-2e63-45f9-b699-71c30b7f8009)

<br>

## 5. 핵심 API
`다수의 좌표들에서 주어진 시간내에 도달 가능한 지하철역들 반환`
![image](https://github.com/k-ms1998/HomeFinder/assets/71029015/e3b4d1c7-3480-4039-9405-dcfd4123f0e0)

`다수의 좌표들에서 주어진 시간내에 도달 가능한 아파트들 반환`
![image](https://github.com/k-ms1998/HomeFinder/assets/71029015/115a9eea-eadb-416a-a997-f2905c03ca22)

`지하철역 이름으로 검색해서 두 개의 지하쳘역 간의 도달하는데 걸리는 시간 반`
![image](https://github.com/k-ms1998/HomeFinder/assets/71029015/6bf077cf-68ac-4024-a11d-471e95a63bcf)
