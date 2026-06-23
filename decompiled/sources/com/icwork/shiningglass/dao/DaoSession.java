package com.icwork.shiningglass.dao;

import com.icwork.shiningglass.dao.bean.Device;
import com.icwork.shiningglass.dao.bean.InputTextRecord;
import com.icwork.shiningglass.model.bean.DiyData;
import com.icwork.shiningglass.model.bean.HistoryData;
import java.util.Map;
import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

/* JADX INFO: loaded from: classes.dex */
public class DaoSession extends AbstractDaoSession {
    private final DeviceDao deviceDao;
    private final DaoConfig deviceDaoConfig;
    private final DiyDataDao diyDataDao;
    private final DaoConfig diyDataDaoConfig;
    private final HistoryDataDao historyDataDao;
    private final DaoConfig historyDataDaoConfig;
    private final InputTextRecordDao inputTextRecordDao;
    private final DaoConfig inputTextRecordDaoConfig;

    public DaoSession(Database database, IdentityScopeType identityScopeType, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig> map) {
        super(database);
        DaoConfig daoConfigM1903clone = map.get(DeviceDao.class).clone();
        this.deviceDaoConfig = daoConfigM1903clone;
        daoConfigM1903clone.initIdentityScope(identityScopeType);
        DaoConfig daoConfigM1903clone2 = map.get(InputTextRecordDao.class).clone();
        this.inputTextRecordDaoConfig = daoConfigM1903clone2;
        daoConfigM1903clone2.initIdentityScope(identityScopeType);
        DaoConfig daoConfigM1903clone3 = map.get(DiyDataDao.class).clone();
        this.diyDataDaoConfig = daoConfigM1903clone3;
        daoConfigM1903clone3.initIdentityScope(identityScopeType);
        DaoConfig daoConfigM1903clone4 = map.get(HistoryDataDao.class).clone();
        this.historyDataDaoConfig = daoConfigM1903clone4;
        daoConfigM1903clone4.initIdentityScope(identityScopeType);
        DeviceDao deviceDao = new DeviceDao(daoConfigM1903clone, this);
        this.deviceDao = deviceDao;
        InputTextRecordDao inputTextRecordDao = new InputTextRecordDao(daoConfigM1903clone2, this);
        this.inputTextRecordDao = inputTextRecordDao;
        DiyDataDao diyDataDao = new DiyDataDao(daoConfigM1903clone3, this);
        this.diyDataDao = diyDataDao;
        HistoryDataDao historyDataDao = new HistoryDataDao(daoConfigM1903clone4, this);
        this.historyDataDao = historyDataDao;
        registerDao(Device.class, deviceDao);
        registerDao(InputTextRecord.class, inputTextRecordDao);
        registerDao(DiyData.class, diyDataDao);
        registerDao(HistoryData.class, historyDataDao);
    }

    public void clear() {
        this.deviceDaoConfig.clearIdentityScope();
        this.inputTextRecordDaoConfig.clearIdentityScope();
        this.diyDataDaoConfig.clearIdentityScope();
        this.historyDataDaoConfig.clearIdentityScope();
    }

    public DeviceDao getDeviceDao() {
        return this.deviceDao;
    }

    public InputTextRecordDao getInputTextRecordDao() {
        return this.inputTextRecordDao;
    }

    public DiyDataDao getDiyDataDao() {
        return this.diyDataDao;
    }

    public HistoryDataDao getHistoryDataDao() {
        return this.historyDataDao;
    }
}
