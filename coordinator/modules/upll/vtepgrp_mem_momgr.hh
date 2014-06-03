/*
 * Copyright (c) 2012-2014 NEC Corporation
 * All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 */

#ifndef UNC_UPLL_VTEP_GRP_MEM_MOMGR_H
#define UNC_UPLL_VTEP_GRP_MEM_MOMGR_H

#include "momgr_impl.hh"
#include "vnode_child_momgr.hh"
#include "vtep_grp_momgr.hh"
#include "vtep_momgr.hh"

namespace unc {
namespace upll {
namespace kt_momgr {

enum VtepGrpMemMoMgrTables {
  VTEPGRPMEMTBL = 0,
  NVTEPGRPMEMTABLES
};


class VtepGrpMemMoMgr : public VnodeChildMoMgr {
 private:
  static BindInfo       vtepgrp_mem_bind_info[];
//  static BindInfo       key_vtepgrp_mem_maintbl_update_bind_info[];
  /**
   * @brief  Gets the valid array position of the variable in the value 
   *         structure from the table in the specified configuration  
   *
   * @param[in]     val      pointer to the value structure 
   * @param[in]     indx     database index for the variable
   * @param[out]    valid    position of the variable in the valid array - 
   *                          NULL if valid does not exist.
   * @param[in]     dt_type  specifies the configuration
   * @param[in]     tbl      specifies the table containing the given value 
   *
   **/
  upll_rc_t GetValid(void *val, uint64_t indx, uint8_t *&valid,
      upll_keytype_datatype_t dt_type, MoMgrTables tbl ) {
    UPLL_FUNC_TRACE;
    valid = NULL;
    return UPLL_RC_SUCCESS;
  }
  upll_rc_t UpdateConfigStatus(ConfigKeyVal *vtepgrpmem_key,
                               unc_keytype_operation_t op,
                               uint32_t result,
                               ConfigKeyVal *upd_key,
                               DalDmlIntf *dmi,
                               ConfigKeyVal *ctrlr_key = NULL);

  upll_rc_t UpdateAuditConfigStatus(unc_keytype_configstatus_t cs_status,
                                    uuc::UpdateCtrlrPhase phase,
                                    ConfigKeyVal *&ckv_running,
                                    DalDmlIntf *dmi);

  bool CompareValidValue(void *&val1, void *val2, bool audit) {
    return true;
  }

  upll_rc_t GetControllerDomainId(ConfigKeyVal *ikey,
                                  upll_keytype_datatype_t dt_type,
                                  controller_domain_t *ctrlr_dom,
                                  DalDmlIntf *dmi);

  upll_rc_t ValidateAttribute(ConfigKeyVal *kval,
                              DalDmlIntf *dmi,
                              IpcReqRespHeader *req = NULL);
  /**
     * @brief  Duplicates the input configkeyval including the key and val.  
     * based on the tbl specified.
     *
     * @param[in]  okey   Output Configkeyval - allocated within the function
     * @param[in]  req    Input ConfigKeyVal to be duplicated.
     * @param[in]  tbl    specifies if the val structure belongs to the main table/ controller table or rename table.
     *
     * @retval         UPLL_RC_SUCCESS      Successfull completion.
     * @retval         UPLL_RC_ERR_GENERIC  Failure case.
     **/
  upll_rc_t DupConfigKeyVal(ConfigKeyVal *&okey,
                   ConfigKeyVal *&req, MoMgrTables tbl = MAINTBL);
  /**
     * @brief  Allocates for the specified val in the given configuration in the     * specified table.   
     *
     * @param[in/out]  ck_val   Reference pointer to configval structure 
     *                          allocated.      
     * @param[in]      dt_type  specifies the configuration candidate/running/
     *                          state 
     * @param[in]      tbl      specifies if the corresponding table is the  
     *                          main table / controller table or rename table.
     *
     * @retval         UPLL_RC_SUCCESS      Successfull completion.
     * @retval         UPLL_RC_ERR_GENERIC  Failure case.
     **/
  upll_rc_t AllocVal(ConfigVal *&ck_val, upll_keytype_datatype_t dt_type,
          MoMgrTables tbl = MAINTBL);
/**
    * @brief      Method to get a configkeyval of a specified keytype from an input configkeyval
    *
    * @param[in/out]  okey                 pointer to output ConfigKeyVal 
    * @param[in]      parent_key           pointer to the configkeyval from which the output configkey val is initialized.
    *
    * @retval         UPLL_RC_SUCCESS      Successfull completion.
    * @retval         UPLL_RC_ERR_GENERIC  Failure case.
    */
  upll_rc_t GetChildConfigKey(ConfigKeyVal *&okey,
              ConfigKeyVal *parent_key);
/**
    * @brief      Method to get a configkeyval of the parent keytype 
    *
    * @param[in/out]  okey           pointer to parent ConfigKeyVal 
    * @param[in]      ikey           pointer to the child configkeyval from 
    * which the parent configkey val is obtained.
    *
    * @retval         UPLL_RC_SUCCESS      Successfull completion.
    * @retval         UPLL_RC_ERR_GENERIC  Failure case.
    **/
  upll_rc_t GetParentConfigKey(ConfigKeyVal *&okey, ConfigKeyVal *ikey);

  /**
   * @Brief Get the VtepConfigKeyVal from VtepTbl 
   *
   * @param[in] ikey                      ikey contains key and value structure.
   * @param[in] okey                      okey contains key and value structure.
   * @param[in] dt_type                   database type
   * @param[in] dmi                       pointer to DalDmlIntf
   *
   * @retval UPLL_RC_SUCCESS                      Successful.
   * @retval UPLL_RC_ERR_NOT_ALLOWED_FOR_THIS_DT  Operation not allowed
   * @retval UPLL_RC_ERR_GENERIC          Generic failure.
   */
  upll_rc_t GetVtepConfigValData(ConfigKeyVal *ikey, ConfigKeyVal *&okey,
                                              upll_keytype_datatype_t dt_type,
                                              DalDmlIntf *dmi);
  /**
   * @Brief Validates the syntax of the specified key and value structure
   *        for KT_VTEP_GRP_MEMBER keytype
   *
   * @param[in] req                       This structure contains
   *                                      IpcReqRespHeader(first 8 fields of input request structure).
   * @param[in] ikey                      ikey contains key and value structure.
   *
   * @retval UPLL_RC_SUCCESS              Successful.
   * @retval UPLL_RC_ERR_CFG_SYNTAX       Syntax error.
   * @retval UPLL_RC_ERR_NO_SUCH_INSTANCE key struct is not available.
   * @retval UPLL_RC_ERR_GENERIC          Generic failure.
   * @retval UPLL_RC_ERR_INVALID_OPTION1  option1 is not valid.
   * @retval UPLL_RC_ERR_INVALID_OPTION2  option2 is not valid.
   */
  upll_rc_t ValidateMessage(IpcReqRespHeader *req, ConfigKeyVal *ikey);

  /**
   * @Brief Validates the syntax for KT_VTEP_GRP_MEMBER keytype key structure.
   *
   * @param[in] key_vtep_grp_member KT_VTEP_GRP_MEMBER key structure.
   *
   * @retval UPLL_RC_SUCCESS        validation succeeded.
   * @retval UPLL_RC_ERR_CFG_SYNTAX validation failed.
   */
  upll_rc_t ValidateVTepGrpMemberKey(
      key_vtep_grp_member_t *key_vtep_grp_member,
      uint32_t operation);
  /**
   * @Brief Checks if the specified key type(KT_VTEP_GRP_MEMBER) and
   *        associated attributes are supported on the given controller,
   *        based on the valid flag
   *
   * @param[in] req               This structure contains
   *                              IpcReqRespHeader(first 8 fields of input request structure).
   * @param[in] ikey              ikey contains key and value structure.
   * @param[in] crtlr_name        Controller name.
   *
   * @retval  UPLL_RC_SUCCESS             Validation succeeded.
   * @retval  UPLL_RC_ERR_GENERIC         Validation failure.
   * @retval  UPLL_RC_ERR_INVALID_OPTION1 Option1 is not valid.
   * @retval  UPLL_RC_ERR_INVALID_OPTION2 Option2 is not valid.
   */

  upll_rc_t ValidateCapability(IpcReqRespHeader *req, ConfigKeyVal *ikey,
      const char * crtlr_name);

  upll_rc_t CopyToConfigKey(ConfigKeyVal *&okey, ConfigKeyVal *ikey) {
    UPLL_LOG_INFO("Not supported for this keytype. Returning Generic Error");
    return UPLL_RC_ERR_GENERIC;
  }
  bool GetRenameKeyBindInfo(unc_key_type_t key_type, BindInfo *&binfo,
                                    int &nattr,
                                    MoMgrTables tbl ) {
    return true;
  }
  upll_rc_t IsReferenced(ConfigKeyVal *ikey,
           upll_keytype_datatype_t dt_type, DalDmlIntf *dmi) {
    return UPLL_RC_SUCCESS;
  }

 public:
  VtepGrpMemMoMgr();
  virtual ~VtepGrpMemMoMgr() {
    for (int i = VTEPGRPMEMTBL; i < NVTEPGRPMEMTABLES; i++)
      if (table[i]) {
        delete table[i];
      }
    delete[] table;
  }
  /**
    * @brief      Method to check if individual portions of a key are valid
    *
    * @param[in/out]  ikey                 pointer to ConfigKeyVal referring to a UNC resource
    * @param[in]      index                db index associated with the variable
    *
    * @retval         true                 input key is valid
    * @retval         false                input key is invalid.
    **/
  bool IsValidKey(void *tkey, uint64_t index);

  /**
   * @Brief Compare if Vtep member ControllerId and VtepGrp ControllerId are same
   *        If same throw an error
   *
   * @param[in] req                       pointer IpcReqResp header
   * @param[in] ikey                      ikey contains key and value structure.
   * @param[in] dt_type                   database type
   * @param[in] dmi                       pointer to DalDmlIntf
   *
   * @retval UPLL_RC_SUCCESS                      Successful.
   * @retval UPLL_RC_ERR_NOT_ALLOWED_FOR_THIS_DT  Operation not allowed
   * @retval UPLL_RC_ERR_GENERIC          Generic failure.
   */
  upll_rc_t CompareControllers(ConfigKeyVal *ikey,
                           upll_keytype_datatype_t dt_type,
                           DalDmlIntf *dmi);
};


}  // namespace vtn
}  // namespace upll
}  // namespace unc
#endif
