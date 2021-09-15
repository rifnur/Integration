package com.invitro.integration.motconsu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;


public class CreateMotconsu {
    public static void createMotconsu (String PatientID,String model_id,String datestr,String dir_answ_id, String ModeleName,String fileName) {

        try {
            String url = "jdbc:sqlserver://192.168.7.100\\mssqlserver:1433;DatabaseName=IZHEVSK";
            Connection conn = DriverManager.getConnection(url,"sa","medik17@");

            String query =
                   "declare @mod_id int " +
                          " exec up_get_id 'MOTCONSU', 1, @mod_id output  " +
                          " insert into MOTCONSU (MOTCONSU_ID ,DATE_CONSULTATION ,MODIFY_DATE_TIME,MODELS_ID ,MEDECINS_MODIFY_ID " +
                          " ,PATIENTS_ID ,KRN_CREATE_USER_ID ,REC_STATUS ,FM_DEP_ID ,CREATE_DATE_TIME ,MEDECINS_CREATE_ID,EV_CLOSE " +
                          " ,EV_GOSP,MEDECINS_ID,PUBLISHED,CHANGED,CONS_STATUS,KRN_MODIFY_USER_ID,KRN_MODIFY_DATE,KRN_CREATE_DATE)" +
                          " values(@mod_id,GETDATE(),GETDATE(),'"+model_id+"',672,'" + PatientID + "',672,'W',146,GETDATE(),672,0,0,672,0, " +
                          " 0,'N',672,GETDATE(),GETDATE())" +
                          " declare @image_id int, @imagestext varchar(15) " +
                          " exec up_get_id 'IMAGES', 1, @image_id output " +
                          " set @imagestext=convert(varchar(15),@image_id)+'.pdf' " +
                          " insert into IMAGES (Images_ID ,PATIENTS_ID ,Rubrics_ID,Date_Consultation ,Descriptor " +
                          " ,FileName ,MOTCONSU_ID ,VIRTUAL_DISKS_ID ,MEDECINS_CREATE_ID ,FOLDER,ExternalFile,PUBLISHED) " +
                          " values(@image_id,'"+PatientID+"',200,GETDATE(),'"+fileName+"'+'.pdf','"+fileName+"'+'.pdf',@mod_id, " +
                          " 1,672,'"+datestr+"'+'\\'+'"+PatientID+"'+'\\',0,0) " +
                          " if '"+dir_answ_id+"' >1 begin "+
                          " update DIR_ANSW set MOTCONSU_RESP_ID=@mod_id,ANSW_STATE=1,COMPLETED_DATE=GETDATE(), " +
                          " KRN_MODIFY_DATE = convert(varchar(30),GetDate(),20), KRN_MODIFY_USER_ID = 672 where DIR_ANSW_ID="+dir_answ_id+" end";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.executeUpdate();


            conn.close();
        } catch (Exception e) {
            System.err.println("Ошибка! ");
            System.err.println(e.getMessage());
        }

    }
}
