package com.enginerds.services;

import com.enginerds.domain.*;

public interface SanctionService {
    public SanctionList saveSanctionEntry(SanctionList sanctionList) throws Exception;
    public void readSanctionList(String filename);
    public boolean CheckSanctionList(Transaction transaction);
}