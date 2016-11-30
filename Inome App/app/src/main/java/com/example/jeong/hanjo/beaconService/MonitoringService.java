package com.example.jeong.hanjo.beaconService;

import com.perples.recosdk.RECOBeacon;
import com.perples.recosdk.RECOBeaconRegion;
import com.perples.recosdk.RECOBeaconRegionState;
import com.perples.recosdk.RECOErrorCode;
import com.perples.recosdk.RECOMonitoringListener;

import java.util.Collection;

/**
 * Created by Jeong on 2016-11-06.
 */
public class MonitoringService implements RECOMonitoringListener {

    @Override
    public void didEnterRegion(RECOBeaconRegion recoBeaconRegion, Collection<RECOBeacon> collection) {
        //monitoring 시작 후에 monitoring 중인 region에 들어갈 경우 이 callback 메소드가 호출됩니다.
        // 0.2 버전부터 이 callback 메소드가 호출 될 경우,  recoRegion에서 감지된 비콘들을 전달합니다.
        //region 입장시 코드 작성
    }

    @Override
    public void didExitRegion(RECOBeaconRegion recoBeaconRegion) {
        //monitoring 시작 후에 monitoring 중인 region에서 나올 경우 이 callback 메소드가 호출됩니다.
        //region 퇴장시 코드 작성
    }

    @Override
    public void didStartMonitoringForRegion(RECOBeaconRegion recoBeaconRegion) {
        //monitoring 시작 후에 monitoring을 시작하고 이 callback 메소드가 호출됩니다.
        //monitoring 정상 실행 시 코드 작성
    }

    @Override
    public void didDetermineStateForRegion(RECOBeaconRegionState recoBeaconRegionState, RECOBeaconRegion recoBeaconRegion) {
        //monitoring 시작 후에 monitoring 중인 region에 들어가거나 나올 경우
        //(region 의 상태에 변화가 생긴 경우) 이 callback 메소드가 호출됩니다.
        //didEnterRegion, didExitRegion callback 메소드와 함께 호출됩니다.
        //region 상태 변화시 코드 작성
    }

    @Override
    public void monitoringDidFailForRegion(RECOBeaconRegion recoBeaconRegion, RECOErrorCode recoErrorCode) {
        //monitoring이 정상적으로 시작하지 못했을 경우 이 callback 메소드가 호출됩니다.
        //RECOErrorCode는 "Error Code" 를 확인하시기 바랍니다.
        //monitoring 실패 시 코드 작성
    }
}
