package example.oldExamplesWithKubernetesOfficialClient;

import io.kubernetes.client.custom.V1Patch;
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

public class ModifyLabelOfPod {
    public static void main(String[] args) throws FileNotFoundException, IOException, ApiException {
        // file path to your KubeConfig - change the location to your kubeconfig file
        String kubeConfigPath = "/home/emilien/.kube/config";

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
        V1Pod firstPod = list.getItems().get(0); // get first pod
        System.out.println("Pod name: " + firstPod.getMetadata().getName());
        String body = generateBody("occupied", "true"); //the label
        V1Patch patch = new V1Patch(body); //create patch object from body
        api.patchNamespacedPod(firstPod.getMetadata().getName(), "minecraft", patch, null, null, null, null); //send the patch to kubernetes
    }

    // method for generating a body for patching the pod
    public static String generateBody(String labelKey, String newLabelValue){
        return String.format("[{\"op\": \"add\", \"path\": \"/metadata/labels/%s\", \"value\": \"%s\"}]", labelKey, newLabelValue);
    }
}
