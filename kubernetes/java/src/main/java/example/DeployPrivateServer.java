package example;

import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientException;
import io.fabric8.kubernetes.client.dsl.base.CustomResourceDefinitionContext;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import org.json.JSONObject;

public class DeployPrivateServer {
    public static void main(String[] args) throws FileNotFoundException, IOException {
        try {
            // Initialize the Kubernetes client library
            KubernetesClient client = new DefaultKubernetesClient();

            // Custom Resource Definition is a way to interact with a custom block of the
            // API of k8s provided by an external actor
            CustomResourceDefinitionContext privateSrvCrdContext = new CustomResourceDefinitionContext.Builder()
                    .withName("helmreleases.helm.fluxcd.io").withGroup("helm.fluxcd.io").withScope("Namespaced")
                    .withVersion("v1").withPlural("helmreleases").build();
            // Load the helmrelease from a file - Good because no hardcoding
            Map<String, Object> helmReleaseObjectFromYAML = client.customResource(privateSrvCrdContext)
                    .load(new FileInputStream(new File("src/main/resources/DeployPrivateServer.yaml")));

            // Load the helmrelease as a RAW json - Not good because hardcoding
            String helmReleaseObjectFromRAWJson = "{" + "\"metadata\": {" + "\"name\": \"podinfo\"" + "},"
                    + "\"apiVersion\": \"helm.fluxcd.io/v1\"," + "\"kind\": \"HelmRelease\"," + "\"spec\": {"
                    + "\"chart\": {" + "\"repository\": \"https://stefanprodan.github.io/podinfo\","
                    + "\"name\": \"podinfo\"," + "\"version\": \"3.2.0\"" + "}" + "}" + "}";

            // Create a JSONObject in order to programmatically add a server name, operator name and so on.
            JSONObject helmReleaseJSONObject = new JSONObject(helmReleaseObjectFromYAML);
            helmReleaseJSONObject.getJSONObject("metadata").put("name", "indicado");

            // Send the JSON Object to the k8s API
            client.customResource(privateSrvCrdContext).create("minecraft", helmReleaseJSONObject.toString());

            // Close the kubernetes client (we can't reuse it after that) - Optional because the library close it itself
            client.close();
        } catch (KubernetesClientException exception) {
            System.out.println(exception);
        }
    }
}
