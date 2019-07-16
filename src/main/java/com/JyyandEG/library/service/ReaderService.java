package com.JyyandEG.library.service;


import com.JyyandEG.library.Const.BookState;
import com.JyyandEG.library.Const.InsertState;
import com.JyyandEG.library.Util.BookAndReaderUtil;
import com.JyyandEG.library.bookmapper.BookMapper;
import com.JyyandEG.library.entity.*;
import com.JyyandEG.library.readerMapper.ReaderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReaderService {

    @Autowired
    ReaderMapper readerMapper;

    @Autowired
    BookMapper bookMapper;

    private Byte[] lc = new Byte[0]; //查询锁
    private Byte[] lcc = new Byte[0]; //插入锁


    private int havaEnoughBookNumber(BIAndRIWithBookState biAndRIWithBookState) {
        int number = 0;
        synchronized (lc) {
            number = bookMapper.getBookNumberByBookId(biAndRIWithBookState.getBookid());
        }
        return number;
    }

    private  Integer checkBookIsReturn(BIAndRIWithBookState biAndRIWithBookState){
        BIAndRI biAndRI = new BIAndRI();
        biAndRI.setBookid(biAndRIWithBookState.getBookid());
        biAndRI.setReaderid(biAndRIWithBookState.getReaderid());
       Integer state= bookMapper.getBookStateByRIAndBI(biAndRI);
         return state;
    }

    public List<Reader> getAllReader() {
        return readerMapper.getAllReader();
    }

    public boolean checkReaderPassById(int id, String pass) {

        ReaderWithinIdAndPass readerWithinIdAndPass = null;

        readerWithinIdAndPass = readerMapper.getReaderById(id);
        if (readerWithinIdAndPass == null)
            return false;

        else {
            return pass.equals(readerWithinIdAndPass.getReaderpass());
        }

    }

    public List<BookStateWithReader> getReaderCordByReaderId(String readerid) {
        return BookAndReaderUtil.BsWRiToBsWR(readerMapper.getReaderCordByReaderId(readerid));
    }

    public String applyABook(BIAndRIWithBookState biAndRIWithBookState) {

       Integer state= checkBookIsReturn(biAndRIWithBookState);
        if (state!=null&&(state==BookState.isBorrowed||state==BookState.isApplying)){


           return  "isBorrowedOrApllying";
        }
        if (havaEnoughBookNumber(biAndRIWithBookState) > 0) {
            int isInsert = 0;
            synchronized (lcc) {
                isInsert = readerMapper.applyABook(biAndRIWithBookState);
            }
            if (isInsert > 0) {
                return InsertState.success;
            } else {
                return InsertState.fail;
            }

        } else {
            return InsertState.fail;
        }

    }
}
