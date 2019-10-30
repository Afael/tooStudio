package com.desktopip.exploriztic.tootanium.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.desktopip.exploriztic.tootanium.R;
import com.desktopip.exploriztic.tootanium.activities.SpatialAppCompat;
import com.desktopip.exploriztic.tootanium.adapters.FileExplorerDownloadedAdapter;
import com.desktopip.exploriztic.tootanium.customisable.NewAndRename;
import com.desktopip.exploriztic.tootanium.utilities.StringHelper;
import com.desktopip.exploriztic.tootanium.utilities.Utils;
import com.karan.churi.PermissionManager.PermissionManager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by Jayus on 01/08/2018.
 */

public class FragFileExplorerDownloaded extends Fragment {

    private String root;
    List<String> itemFiles;
    List<String> path;
    List<String> files;
    List<String> filesPath;
    String currDirectory;
    File isFile;

    PermissionManager permissionManager;
    private boolean permissionStatus = false;

    Toolbar feDownToolbar;
    ListView rootList;
    RelativeLayout local_no_content;
    FileExplorerDownloadedAdapter fileExplorerDownloadedAdapter;

    private ClipboardManager clipboardManager;
    private ClipData clipData;

    InputStream in;
    OutputStream out;
    File m_copyFile;
    List<String> listCopy, listDelete;

    NewAndRename newAndRenameDialog;
    Bundle newAndRenameBundle;

    private static FragFileExplorerDownloaded instance = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        permissionStatus = hasPermissions(getContext(), new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
        });

        if (!permissionStatus) {
            permissionManager = new PermissionManager() {
            };
            permissionManager.checkAndRequestPermissions(getActivity());
        }

        View view = inflater.inflate(R.layout.frag_file_explore_local_list, container, false);
        initialize(view);
        newAndRenameDialog = new NewAndRename();
        instance = this;
        return view;
    }

    public static FragFileExplorerDownloaded getInstance() {
        return instance;
    }


    public static boolean hasPermission(Context context, String permission) {

        int res = context.checkCallingOrSelfPermission(permission);

        Log.v("PERMISSION", "permission: " + permission + " = \t\t" +
                (res == PackageManager.PERMISSION_GRANTED ? "GRANTED" : "DENIED"));

        return res == PackageManager.PERMISSION_GRANTED;

    }

    public static boolean hasPermissions(Context context, String... permissions) {

        boolean hasAllPermissions = true;

        for (String permission : permissions) {
            //you can return false instead of assigning, but by assigning you can log all permission values
            if (!hasPermission(context, permission)) {
                hasAllPermissions = false;
            }
        }

        return hasAllPermissions;

    }

    private void initialize(View view) {

        if (permissionStatus) {
            listCopy = new ArrayList<>();
            listDelete = new ArrayList<>();
            feDownToolbar = view.findViewById(R.id.local_toolbar);
            feDownToolbar.setTitle("Download");
            feDownToolbar.setSubtitle("Browse your File here");

            SpatialAppCompat.setActionBarColor(feDownToolbar, getContext());

            ((AppCompatActivity) getActivity()).setSupportActionBar(feDownToolbar);
            setHasOptionsMenu(true);

            rootList = view.findViewById(R.id.rootList);
            local_no_content = view.findViewById(R.id.local_no_content);

            clipboardManager = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);

            root = Environment.getExternalStorageDirectory().getPath() + "/" + Utils.downloadDirectory;

            File dir = new File(root);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            getDirFromRoot(root);
            createFolderAsset();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionManager.checkResult(requestCode, permissions, grantResults);
    }

    private File getDirectory() {
        File directory = Environment.getExternalStorageDirectory();
        return new File(directory.getAbsolutePath() + "/.temp");
    }

    private void createFolderAsset() {
        File folder = getDirectory();
        boolean success = false;
        if (!folder.exists()) {
            success = folder.mkdirs();
        }
    }

    public void getDirFromRoot(String rootPath) {

        itemFiles = new ArrayList<>();
        Boolean isRoot = true;
        path = new ArrayList<>();
        files = new ArrayList<>();
        filesPath = new ArrayList<>();
        File rootDir = new File(rootPath);
        File[] filesArray = rootDir.listFiles();

        if (!rootPath.equals(root)) {
            itemFiles.add("../");
            path.add(rootDir.getParent());
            isRoot = false;
        }
        currDirectory = rootPath;

        //if(filesArray.length > 0){
        local_no_content.setVisibility(View.GONE);
        //sorting file list in alphabetical order
        Arrays.sort(filesArray);
        for (int i = 0; i < filesArray.length; i++) {
            File file = filesArray[i];
            if (file.isDirectory()) {
                itemFiles.add(file.getName());
                path.add(file.getPath());
            } else {
                files.add(file.getName());
                filesPath.add(file.getPath());
            }
        }

        for (String m_AddFile : files) {
            itemFiles.add(m_AddFile);
        }

        for (String m_AddPath : filesPath) {
            path.add(m_AddPath);
        }

        if (itemFiles.size() > 0) {
            rootList.setVisibility(View.VISIBLE);
            local_no_content.setVisibility(View.GONE);
            fileExplorerDownloadedAdapter = new FileExplorerDownloadedAdapter(getActivity(), itemFiles, path, isRoot);
            rootList.setAdapter(fileExplorerDownloadedAdapter);
        } else {
            rootList.setVisibility(View.GONE);
            local_no_content.setVisibility(View.VISIBLE);
        }


        rootList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                isFile = new File(path.get(position));
                Log.d("path", "" + path.get(position));

                if (isFile.isDirectory()) {

                    getDirFromRoot(isFile.toString());

                } else {

                    Intent openIntent = new Intent(Intent.ACTION_VIEW);
                    openIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    openIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    openIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                    Uri appName = FileProvider.getUriForFile(getActivity(), getActivity()
                            .getApplicationContext()
                            .getPackageName() + ".provider", isFile);

                    String[] mimeTypes = getResources().getStringArray(R.array.mime_type);

                    String[] ext = getResources().getStringArray(R.array.extension);

                    if (mimeTypes.length > 0) {
                        openIntent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
                        openIntent.setData(appName);
                    }

                    String rootDirpath = StringHelper.substringAfterLast(isFile.getAbsolutePath(), "/");
                    if (rootDirpath.contains(".")) {
                        String type = rootDirpath.substring(rootDirpath.lastIndexOf("."));

                        for (String sExt : ext) {
                            if (type.equalsIgnoreCase(sExt)) {
                                if (Utils.isAppAvailable(getActivity(), openIntent)) {
                                    getActivity().startActivity(openIntent);
                                } else {
                                    Toast.makeText(getActivity(), "Can't open file, to open this file we need to know what program you want to use to open it", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                }
            }
        });
        //}
        //else {
        //getDirFromRoot(currDirectory);
        //local_no_content.setVisibility(View.VISIBLE);
        //}

    }

    void deleteFile() {

        for (String sDelete : listDelete) {
            File deleteFile = new File(sDelete);
            if (deleteFile.isDirectory()) {
                if (deleteRecursiveFolder(deleteFile))
                    Toast.makeText(getActivity(), "File(s) Deleted", Toast.LENGTH_SHORT).show();
            } else {
                //Log.d("file",path.get(m_delItem));
                boolean m_isDelete = deleteFile.delete();
                if (m_isDelete)
                    Toast.makeText(getActivity(), "File(s) Deleted", Toast.LENGTH_SHORT).show();
            }
        }
        listDelete.clear();
        getDirFromRoot(currDirectory);

    }

    private boolean deleteRecursiveFolder(File rootFolder) {

        if (rootFolder.isDirectory()) {
            String[] children = rootFolder.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteRecursiveFolder(new File(rootFolder, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return rootFolder.delete();
    }

    private void createNewFolder(final int anuAn) {

        newAndRenameBundle = new Bundle();
        newAndRenameBundle.putString("rt", "newFolderDownloaded");
        newAndRenameBundle.putString("anuAn", String.valueOf(anuAn));
        newAndRenameBundle.putString("currDirectory", currDirectory);

        if (!newAndRenameDialog.isVisible()) {
            newAndRenameDialog.setArguments(newAndRenameBundle);
            newAndRenameDialog.show(getActivity().getSupportFragmentManager(), "ChannelSettingsDialog");
        }

    }

    private void unzip(File zipFile, File targetDirectory) throws IOException {

        ZipInputStream zis = new ZipInputStream(
                new BufferedInputStream(new FileInputStream(zipFile)));

        try {
            ZipEntry ze;
            int count;
            byte[] buffer = new byte[8192];
            while ((ze = zis.getNextEntry()) != null) {
                File file = new File(targetDirectory, ze.getName());
                File dir = ze.isDirectory() ? file : file.getParentFile();
                if (!dir.isDirectory() && !dir.mkdirs()) {
                    //alertDialog.dismiss();
                    throw new FileNotFoundException("Failed to ensure directory: " + dir.getAbsolutePath());
                }
                if (ze.isDirectory())
                    continue;
                FileOutputStream fOut = new FileOutputStream(file);
                try {
                    while ((count = zis.read(buffer)) != -1) {
                        fOut.write(buffer, 0, count);
                    }
                    //alertDialog.dismiss();
                } finally {
                    //alertDialog.dismiss();
                    fOut.close();
                }
            }
        } finally {
            zis.close();
        }
    }

    private void copyToTemp(File fileCopy) throws IOException {
        //List<String> listCopy = new ArrayList<>();
        //String fileName = fileCopy.toString();
        //File folder = getDirectory();
        //File captured = new File(folder.getPath() + "/" + StringHelper.substringAfterLast(fileName, "/"));
        //clipData = ClipData.newPlainText("pathTemp", fileName);
        //clipboardManager.setPrimaryClip(clipData);

        in = new FileInputStream(fileCopy);
//        OutputStream out = new FileOutputStream(captured);
//        try {
//
//            // Transfer bytes from in to out
//            byte[] buf = new byte[1024];
//            int len;
//            while ((len = in.read(buf)) > 0) {
//                out.write(buf, 0, len);
//            }
//            //Uri uri = Uri.parse(captured.toString());
//            clipData = ClipData.newPlainText("pathTemp", fileName);
//            clipboardManager.setPrimaryClip(clipData);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            in.close();
//        }
    }

    private void pasteFromTemp() {

//        clipData = clipboardManager.getPrimaryClip();
//
//        if (clipData != null) {
//            ClipData.Item clipItem = clipData.getItemAt(0);
//            String pathTemp = clipItem.getText().toString();
//            File inputPath = new File(pathTemp);
        try {
////                InputStream in = new FileInputStream(inputPath);
//            //OutputStream out = new FileOutputStream(currDirectory + "/" + StringHelper.substringAfterLast(pathTemp, "/"));
//
//
            if (listCopy.size() > 0) {
                for (String sCopy : listCopy) {
                    Log.d("Paste", "fromList: " + sCopy);
                    m_copyFile = new File(sCopy);

                    in = new FileInputStream(m_copyFile);
                    Log.d("Paste", "pasteFromTemp: " + m_copyFile);
                    out = new FileOutputStream(currDirectory + "/" + StringHelper.substringAfterLast(m_copyFile.toString(), "/"));
                    // Transfer bytes from in to out
                    byte[] buf = new byte[1024];
                    int len;
                    BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(out);
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(in);
                    while ((len = bufferedInputStream.read(buf)) > 0) {
                        bufferedOutputStream.write(buf, 0, len);
                    }

                    in.close();
                    bufferedInputStream.close();
                    bufferedOutputStream.close();
                    out.close();

                }
            } else {
                Toast.makeText(getActivity(), "No file(s) are copied", Toast.LENGTH_SHORT).show();
            }


//                while ((len = in.read(buf)) > 0) {
//                    out.write(buf, 0, len);
//                }
//                Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//                scanIntent.setData(Uri.fromFile(currDirectory));
//                getActivity().sendBroadcast(scanIntent);
//                clipData = ClipData.newPlainText("", "");
//                clipboardManager.setPrimaryClip(clipData);
//                File folder = getDirectory();
//                File captured = new File(folder.getPath() + "/" + StringHelper.substringAfterLast(pathTemp, "/"));
//                captured.delete();


//            Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//            scanIntent.setData(Uri.fromFile(m_copyFile));
//            getActivity().sendBroadcast(scanIntent);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        }
        listCopy.clear();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        feDownToolbar.getMenu().clear();
        feDownToolbar.inflateMenu(R.menu.menu_file_local);

        for (int i = 0; i < menu.size(); i++) {
            Drawable iconToolbar = menu.getItem(i).getIcon(); // change 0 with 1,2 ...
            iconToolbar.mutate();
            iconToolbar.setColorFilter(getResources().getColor(R.color.color_white), PorterDuff.Mode.SRC_IN);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.loc_delete:
                //File deleteFile = null;
                if (itemFiles.size() > 0) {
                    if (fileExplorerDownloadedAdapter.m_selectedItem.size() > 0) {

                        AlertDialog.Builder confirm = new AlertDialog.Builder(getActivity());
                        confirm.setMessage("Are you sure, want to delete?");
                        confirm.setCancelable(false);

                        confirm.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //deleteFile();
                                for (int deleteItem : fileExplorerDownloadedAdapter.m_selectedItem) {
                                    listDelete.add(path.get(deleteItem));
                                }

                                if (listDelete.size() > 0) {
                                    deleteFile();
                                } else {
                                    Toast.makeText(getActivity(), "No file(s) are selected", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                        confirm.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });

                        AlertDialog showDialog = confirm.create();
                        showDialog.show();
                    } else {
                        Toast.makeText(getActivity(), "No file", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getActivity(), "No file(s) are selected", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.loc_new_folder:
                createNewFolder(1);
                break;
            case R.id.loc_extract:
                if (itemFiles.size() > 0) {
                    if (fileExplorerDownloadedAdapter.m_selectedItem.size() > 0) {
                        for (int m_unarchiveItem : fileExplorerDownloadedAdapter.m_selectedItem) {
                            File m_unarchiveFile = new File(path.get(m_unarchiveItem));
                            File curDir = new File(currDirectory);

                            try {
                                unzip(m_unarchiveFile, curDir);
                                //alertDialog.dismiss();
                                Toast.makeText(getActivity(), "File(s) unarchived", Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                //alertDialog.dismiss();
                                Log.d("errorUnarchive", e.getMessage().toString());
                                e.printStackTrace();
                            }
                            getDirFromRoot(currDirectory);
                        }

                    } else {
                        Toast.makeText(getActivity(), "No file(s) are selected", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "No file", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.loc_copy:
                if (itemFiles.size() > 0) {
                    if (fileExplorerDownloadedAdapter.m_selectedItem.size() > 0) {
                        for (int copyItem : fileExplorerDownloadedAdapter.m_selectedItem) {
                            listCopy.add(path.get(copyItem));
                            getDirFromRoot(currDirectory);
                        }
                        getDirFromRoot(currDirectory);
                    } else {
                        Toast.makeText(getActivity(), "No file(s) are selected", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "No file", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.loc_paste:
                pasteFromTemp();
                getDirFromRoot(currDirectory);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int listPosition = info.position;
        fileExplorerDownloadedAdapter.getItem(listPosition);
        return super.onContextItemSelected(item);
    }
}
