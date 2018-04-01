package es.service;

import java.io.File;

/**
 * Created by TYF on 2018/2/26.
 */
public interface SaveService {
    boolean saveDoc(File file);

    boolean saveDocs(File file);
}