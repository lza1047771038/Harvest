package com.harvest.core_network.impl;

/**
 * 具体使用可参考如下示例
 * <p>
 * public class HNetworkAgent {
 *
 * public static List<Statuses> fetchHomeLineStatuses(Object... params) {
 *      return NetworkAgent.getInstance().loadApi("2/statuses/home_timeline.json")
 *              .setParams(params)
 *              .setErrorNotifier(throwable -> ToastHelper.Companion.showToast(throwable.getMessage()))
 *              .executeApi(jsonObject -> {
 *                      final HomeLineResult result = GsonAdapter.getInstance().parseJson(jsonObject, HomeLineResult.class);
 *                      return result.getStatuses();
 *              });
 *      }
 * }
 * </p>
 */
public class NetworkAgent {

    private static NetworkAgent mInstance = null;

    public static NetworkAgent getInstance() {
        if (mInstance == null) {
            synchronized (NetworkAgent.class) {
                if (mInstance == null) {
                    mInstance = new NetworkAgent();
                }
            }
        }
        return mInstance;
    }

    public NetworkRequest loadApi(String url) {
        return new NetworkRequest(Configurations.domainUrl, url);
    }
}
