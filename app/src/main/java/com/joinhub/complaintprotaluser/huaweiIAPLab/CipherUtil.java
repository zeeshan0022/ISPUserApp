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
    private static final String PUBLIC_KEY = "MIIBojANBgkqhkiG9w0BAQEFAAOCAY8AMIIBigKCAYEAo7sVAAEgIHVz7c/xoyMBQy28OVdFMIB/qNoU3U0sSjBgYR1tZCmbBG1wCQuvVI/ZvMx4qLlwAONxtRexz9XckXuaCbhc7Us56tGGUxn9LPjm1AuTexZWVM2i0M1q3P8yeACqRzLlc0oXQyZ2LK7uUbJP/KVpYkbEkFxZ5sf2IdplYcf+PEynO/oum1Uee6l4GICNQbIMwGtiMR8yJeahoH+5lsKI1ZctgaHM04UPKBHYCzyqaiNj7c2d2np5je3tGFh0hMKOC3QkDYnPADm/As3JuIJZL+Szu/n7EUCmVh3IUtUgF2NkegCi01LbKlvn5b7elrZLzb2bNsftOgitWsRyUjd9TZdeoLrWZXidYjaVu2JZ2UtzKQZmlJ7KBYlCEvXU1dFQ+zVAQSry4Nel+dlIxwrEFM7W3NcwzKiZxCkUEJxD89HxXki4pIOPVmge3PMIEJkzGqOvtHm8CliiRuXg/zpT41TPD4sl3rDWNhBy+TPxuzliMJITTnFP13R5AgMBAAE=";

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
