package es.service;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;

/**
 * Created by TYF on 2018/2/26.
 */
public interface SaveService {
    String saveNewDocs();

    boolean saveDoc(File file);

    void saveDocs(String fileLocation) throws IOException, JSONException;
}