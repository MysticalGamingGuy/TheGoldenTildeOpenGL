#version 330 core
out vec4 FragColor;

in vec2 texCord;

uniform sampler2D texture0;
uniform sampler2D texture1;

uniform float amount;

void main(){
    FragColor =  mix(texture(texture0,texCord),texture(texture1,texCord),amount);
}