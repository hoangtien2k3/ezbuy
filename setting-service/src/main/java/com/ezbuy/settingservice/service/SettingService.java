package com.ezbuy.settingservice.service;

import com.ezbuy.settingmodel.model.Setting;
import com.ezbuy.settingmodel.request.CreateSettingRequest;
import com.ezbuy.settingmodel.request.SearchSettingRequest;
import com.ezbuy.settingmodel.response.SearchSettingResponse;
import io.hoangtien2k3.commons.model.response.DataResponse;
import java.util.List;
import reactor.core.publisher.Mono;

public interface SettingService {

    Mono<String> findByCode(String code);

    /**
     * Author: AnhTN Ham nay dung de tim kiem cac cai dat theo param truyen vao
     *
     * @param request
     *            (code(ma), status(trang thai), fromDate(tu ngay), toDate(den
     *            ngay))
     * @return list setting, va phan trang
     */
    Mono<SearchSettingResponse> searchSetting(SearchSettingRequest request);

    /**
     * Author: AnhTN Ham nay dung de tra ve tat ca ban ghi cai dat
     *
     * @return list setting database
     */
    Mono<List<Setting>> getAllSetting();

    /**
     * Author: AnhTn ham nay dung de lay danh sach cac cai dat co hieu luc
     *
     * @return list cac cai dat dang co hieu luc
     */
    Mono<List<Setting>> getAllActiveSetting();

    /**
     * Author: AnhTN ham nay dung de tao ra mot ban ghi cai dat moi
     *
     * @param request
     *            (code(ma), value(gia tri), description(chi tiet), status(trang
     *            thai))
     * @return mot ban ghi cai dat moi
     */
    Mono<DataResponse<Setting>> createSetting(CreateSettingRequest request);

    /**
     * Author: AnhTN ham nay dung de cap nhat ra mot ban ghi cai dat dua tren id cua
     * cai dat do
     *
     * @param id
     *            (id cua cai dat muon cap nhat)
     * @param request
     *            (code(ma), value(gia tri), description(chi tiet), status(trang
     *            thai))
     * @return ban ghi cai dat da sua theo dung thong tin truyen vao
     */
    Mono<DataResponse<Setting>> updateSetting(String id, CreateSettingRequest request);

    /**
     * Author AnhTN ham nay dung de update cai dat thanh khong hieu luc
     *
     * @param id
     *            (id cua cai dat muon update)
     * @return cai dat voi trang thai = "khong hieu luc"
     */
    Mono<DataResponse<Setting>> deleteSetting(String id);
}
