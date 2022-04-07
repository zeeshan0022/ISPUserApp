/**
 * Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.joinhub.complaintprotaluser.huaweiIAPLab;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

/**
 * Signature related tools.
 *
 * @since 2019/12/9
 */
public class CipherUtil {
    private static final String TAG = "CipherUtil";

    // The SHA256WithRSA algorithm.
    private static final String SIGN_ALGORITHMS = "SHA256WithRSA";

    // The Iap public key of this App.
    private static final String PUBLIC_KEY = "MIIBojANBgkqhkiG9w0BAQEFAAOCAY8AMIIBigKCAYEAmOM7pVJfhTieuetecOOOaZh8qKUUFRJsazAVlWWQ5lMCJbIdEDOrRS55+LBdOzGP7DlTP37Nus0SJHyvibw5/hN017l+klIqrEHt37/fCdysswNekfb15zG4Wh87ll+l1GhM2kEKzLIpP+hwegsz6iWVWey6smCdHswO76lJCXdHa+zY/DmyQ2H/i5VqDbfG7Rg9bEYdCmouUvHuN4/H1G3IkEP1lpec5DqPBPQVC7UgH8jjXsrW8fQ7wiYxKxnfczS5iPsyG4iWQdF+x/vk0WlHeX1dnw6vJXPj3SPYI+RXl8WoUgQ+/7pkdWG3CddM8YVHkLBJvrXOr/9IWmBvKFP2hzWKmJnwXeKp0+unx64BuySENKt/HPmbHdjaVB18NS6yLCU9KCd2w4WehA3T4dbwtoj1kWrKhkVV1z9w3QrIEwcUheJXCd46d2icKR9MoCuCk2XkKpFxH8/e9/aBz+oCWfdG1Eit+5feFK05RGvYUKT/lHOatmHAcBdXeBglAgMBAAE=";
    /**
     * The method to check the signature for the data returned from the interface.
     *
     * @param content Unsigned data.
     * @param sign The signature for content.
     * @param publicKey The public of the application.
     * @return boolean
     */
    public static boolean doCheck(String content, String sign, String publicKey) {
        if (TextUtils.isEmpty(publicKey)) {
            Log.e(TAG, "publicKey is null");
            return false;
        }

        if (TextUtils.isEmpty(content) || TextUtils.isEmpty(sign)) {
            Log.e(TAG, "data is error");
            return false;
        }

        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] encodedKey = Base64.decode(publicKey, Base64.DEFAULT);
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));

            java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);

            signature.initVerify(pubKey);
            signature.update(content.getBytes("utf-8"));

            boolean bverify = signature.verify(Base64.decode(sign, Base64.DEFAULT));
            return bverify;

        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "doCheck NoSuchAlgorithmException" + e);
        } catch (InvalidKeySpecException e) {
            Log.e(TAG, "doCheck InvalidKeySpecException" + e);
        } catch (InvalidKeyException e) {
            Log.e(TAG, "doCheck InvalidKeyException" + e);
        } catch (SignatureException e) {
            Log.e(TAG, "doCheck SignatureException" + e);
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "doCheck UnsupportedEncodingException" + e);
        }
        return false;
    }

    /**
     * Get the publicKey of the application.
     * During the encoding process, avoid storing the public key in clear text.
     *
     * @return publickey
     */
    public static String getPublicKey(){
        return PUBLIC_KEY;
    }

}
