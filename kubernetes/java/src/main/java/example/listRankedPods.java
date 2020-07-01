package example;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.KubeConfig;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class listRankedPods {
    public static void main(String[] args) throws FileNotFoundException, IOException, ApiException {
        // file path to your KubeConfig - change the location to your kubeconfig file
        String kubeConfigPath = "/home/emilien/.kube/config.bolt-us-central-1";

        // loading the out-of-cluster config, a kubeconfig from file-system
        ApiClient client = ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new FileReader(kubeConfigPath))).build();

        // set the global default api-client to the in-cluster one from above
        Configuration.setDefaultApiClient(client);

        // the CoreV1Api loads default api-client from global configuration.
        CoreV1Api api = new CoreV1Api();

        // fetch pods with the label app and as a value: ranked
        // documentation : https://kubernetes.io/docs/concepts/overview/working-with-objects/labels/
        String labelSelector = "app=ranked";

        // invokes the CoreV1Api client
        V1PodList list = api.listNamespacedPod("minecraft", null, null, null, null, labelSelector, null, null, null, null);
        for (V1Pod item : list.getItems()) {
            System.out.println(item.getMetadata().getName());
        }
    }
}
