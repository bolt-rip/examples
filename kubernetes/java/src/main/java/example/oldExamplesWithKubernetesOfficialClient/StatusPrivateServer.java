package example.oldExamplesWithKubernetesOfficialClient;

import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientException;
import io.fabric8.kubernetes.client.dsl.base.CustomResourceDefinitionContext;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.json.JSONObject;

public class StatusPrivateServer {
    public static void main(String[] args) {
        try {
            // Initialize the Kubernetes client library
            KubernetesClient client = new DefaultKubernetesClient();

            // Custom Resource Definition is a way to interact with a custom block of the
            // API of k8s provided by an external actor
            CustomResourceDefinitionContext privateSrvCrdContext = new CustomResourceDefinitionContext.Builder()
                    .withName("helmreleases.helm.fluxcd.io").withGroup("helm.fluxcd.io").withScope("Namespaced")
                    .withVersion("v1").withPlural("helmreleases").build();

            // Get full JSON object from the helm release "notch"
            JSONObject helmReleaseStatus = new JSONObject(
                    client.customResource(privateSrvCrdContext).get("minecraft", "notch"));
            // If you get "Succeeded" then the helm release is ready and so is the private server.
            System.out.println(helmReleaseStatus.getJSONObject("status").get("phase"));

            // Close the kubernetes client (we can't reuse it after that) - Optional because
            // the library close it itself
            client.close();
        } catch (KubernetesClientException e) {
            e.printStackTrace();
        }
    }
}
