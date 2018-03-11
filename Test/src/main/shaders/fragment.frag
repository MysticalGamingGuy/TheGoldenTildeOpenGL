#version 330 core
out vec4 FragColor;

in vec2 texCord;

uniform sampler2D texture1;
uniform sampler2D texture2;

uniform float mix;

void main(){
    FragColor =  mix(texture(texture1,texCord),texture(texture2,texCord),mix);
}