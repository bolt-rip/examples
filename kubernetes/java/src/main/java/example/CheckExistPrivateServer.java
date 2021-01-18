package example;

import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientException;
import io.fabric8.kubernetes.client.dsl.base.CustomResourceDefinitionContext;

import java.util.Map;

public class CheckExistPrivateServer {
    public static void main(String[] args) {
        try {
            // Initialize the Kubernetes client library
            KubernetesClient client = new DefaultKubernetesClient();

            // Custom Resource Definition is a way to interact with a custom block of the
            // API of k8s provided by an external actor
            CustomResourceDefinitionContext privateSrvCrdContext = new CustomResourceDefinitionContext.Builder()
                    .withName("helmcharts.helm.cattle.io").withGroup("helm.cattle.io").withScope("Namespaced")
                    .withVersion("v1").withPlural("helmcharts").build();

            String username = "mitchiii_";

            Map<String, Object> helmCharts = client.customResource(privateSrvCrdContext).list("minecraft");

            if (helmCharts.toString().contains("private-" + username.toLowerCase().replaceAll("_", "-") + "-server")) {
                System.out.println("private server exist");
            } else {
                System.out.println("private server doesn't exist");
            }

            // Close the kubernetes client (we can't reuse it after that) - Optional because
            // the library close it itself
            client.close();
        } catch (KubernetesClientException e) {
            e.printStackTrace();
        }
    }
}
