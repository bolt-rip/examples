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
    public static void main(String[] args) {
        try {
            // Initialize the Kubernetes client library
            KubernetesClient client = new DefaultKubernetesClient();

            // Custom Resource Definition is a way to interact with a custom block of the
            // API of k8s provided by an external actor
            CustomResourceDefinitionContext privateSrvCrdContext = new CustomResourceDefinitionContext.Builder()
                    .withName("helmcharts.helm.cattle.io").withGroup("helm.cattle.io").withScope("Namespaced")
                    .withVersion("v1").withPlural("helmcharts").build();

            // Load the helmChart from a file - Good because no hardcoding
            Map<String, Object> helmChartObjectFromYAML = client.customResource(privateSrvCrdContext)
                    .load(new FileInputStream(new File("src/main/resources/DeployPrivateServer.yaml")));

            // Load the helmChart as a RAW json - Not good because hardcoding - lines 32-34 is an old example that doesn't work anymore
            String helmChartObjectFromRAWJson = "{\"metadata\":{\"name\":\"mitchiii--server\"},\"apiVersion\":\"helm.cattle.io/v1\","
                    + "\"kind\":\"HelmChart\",\"spec\":{\"chart\":{\"path\":\"charts/privateserver\",\"ref\":\"master\",\"git\""
                    + ":\"git@github.com:bolt-rip/k8s\"},\"values\":{\"operators\":\"notch:jeb_\",\"maxPlayers\":20}}}";

            String username = "mitchiii_";

            // Create a JSONObject in order to programmatically add a server name, operator
            // name and so on.
            JSONObject helmChartJSONObject = new JSONObject(helmChartObjectFromYAML);
            helmChartJSONObject.getJSONObject("metadata").put("name", "private-" + username.toLowerCase().replaceAll("_", "-") + "-server");
            helmChartJSONObject.getJSONObject("spec").getJSONObject("values").getJSONObject("set").put("config.serverName", username);
            helmChartJSONObject.getJSONObject("spec").getJSONObject("values").getJSONObject("set").put("config.operators", username);

            // Send the JSON Object to the k8s API
            client.customResource(privateSrvCrdContext).create("minecraft", helmChartJSONObject.toString());

            // Close the kubernetes client (we can't reuse it after that) - Optional because
            // the library close it itself
            client.close();
        } catch (

        KubernetesClientException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
