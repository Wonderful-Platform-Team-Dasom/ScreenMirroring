# ScreenMirroring

*Event & Messing 구조
* 현재 앱 형태로 구현되었으며, 서비스 형태로 테스트 개발 예정
* 미러링 요청 화면으로 연결

*사용법
1. ScreenMirroring[:screenmirroing:assembleRelease} 빌드
2. screenmirroring->build->outputs->aar->screenmirroring-release.aar 추출
3. 해당 프로젝트에 screenmirroring-release.aar 라이브러리 추가
4. app gradle에  implementation project(path: ':screenmirroring') 추가

*구조
- 코어 기능 : init/start/stop (ScreenMirroringInterface 정의)
- ScreengMirroringManager : 메시징 처리를 위한 기능 구현. 리스너 등록/ 해제 및 메시징 전달 역할
- ScreenMIrroring : 안드로이드 네이티브 접근
- ScreenMirroringResponseCode, ScreenMirroringResponseState : 메시지 관리
 
