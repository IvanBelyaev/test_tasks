package org.example.tasks.task4;

import java.time.LocalDate;
import java.util.List;

/**
 * Класс для представления строки CSV-файла в виде объекта.
 * Реализует интерфейс Comparable. Сравнивает объекты по полю fid.
 */
public class Data implements Comparable<Data> {
    private final int fid;
    private final int serialNum;
    private final List<String> memberCodes;
    private final int acctType;
    private final LocalDate openedDt;
    private final int acctRteCde;
    private final LocalDate reportingDt;
    private final int creditLimit;

    public Data(int fid, int serialNum, List<String> memberCodes, int acctType,
                LocalDate openedDt, int acctRteCde, LocalDate reportingDt, int creditLimit) {
        this.fid = fid;
        this.serialNum = serialNum;
        this.memberCodes = memberCodes;
        this.acctType = acctType;
        this.openedDt = openedDt;
        this.acctRteCde = acctRteCde;
        this.reportingDt = reportingDt;
        this.creditLimit = creditLimit;
    }

    public int getFid() {
        return fid;
    }

    public int getSerialNum() {
        return serialNum;
    }

    public List<String> getMemberCodes() {
        return memberCodes;
    }

    public int getAcctType() {
        return acctType;
    }

    public LocalDate getOpenedDt() {
        return openedDt;
    }

    public int getAcctRteCde() {
        return acctRteCde;
    }

    public LocalDate getReportingDt() {
        return reportingDt;
    }

    public int getCreditLimit() {
        return creditLimit;
    }

    @Override
    public int compareTo(Data o) {
        return Integer.compare(this.fid, o.getFid());
    }

    @Override
    public String toString() {
        return "Data{" +
                "fid=" + fid +
                ", serialNum=" + serialNum +
                ", memberCodes=" + memberCodes +
                ", acctType=" + acctType +
                ", openedDt=" + openedDt +
                ", acctRteCde=" + acctRteCde +
                ", reportingDt=" + reportingDt +
                ", creditLimit=" + creditLimit +
                '}';
    }
}
