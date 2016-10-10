package seedqr.gui;


import java.util.ArrayList;
import java.util.List;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import seedqr.mapper.QrCodeMapper;
import seedqr.mapper.RegionMapper;
import seedqr.mapper.SaleMapper;
import seedqr.model.QrCode;
import seedqr.model.SaleInfo;
import seedqr.model.User;
import seedqr.util.MybatisUtil;

@Stateless @TransactionManagement(TransactionManagementType.BEAN)
public class StockOutService {

    @Asynchronous
    public void doOut(User user,List<Long> longCodes,int salerId) {
        String region = MybatisUtil.call(RegionMapper.class, regionMapper -> regionMapper.getSalerRegion(salerId));
        for(Long packCode : longCodes) {
            String target = MybatisUtil.call(QrCodeMapper.class, codeMapper -> codeMapper.getTargetsBySrc(packCode.toString()));
            List<QrCode> unitCodes = MybatisUtil.call(QrCodeMapper.class, codeMapper -> codeMapper.getQrCodeByUnitIds(target));
            List<SaleInfo> sales = new ArrayList<>();
            for(QrCode code:unitCodes) {
                SaleInfo sale = new SaleInfo(code.getId(),salerId,"生产商发货至" + region,1);
                sales.add(sale);
            }
            MybatisUtil.run(SaleMapper.class, saleMapper -> saleMapper.addSaleInfos(sales));
        }
    }
}
